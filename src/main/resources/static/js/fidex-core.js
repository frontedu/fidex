/**
 * Fidex Core JS
 */
document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll(".input-group input, .input-group select");
    const updateInputState = (input) => {
        if (input.value) {
            input.classList.add("filled");
        } else {
            input.classList.remove("filled");
        }
    };

    inputs.forEach((input) => {
        updateInputState(input);
        input.addEventListener("change", () => updateInputState(input));
        input.addEventListener("input", () => updateInputState(input));
        input.addEventListener("blur", () => updateInputState(input));
    });

    const forms = document.querySelectorAll("form");
    forms.forEach(form => {
        form.addEventListener("submit", function () {
            const submitBtn = this.querySelector('input[type="submit"], button[type="submit"]');
            if (submitBtn && !submitBtn.classList.contains('no-load')) {
                if (!this.classList.contains('optimistic-form')) {
                    submitBtn.style.opacity = "0.7";
                    submitBtn.style.cursor = "wait";
                }
            }
        });
    });

    const modalToggleUser = document.getElementById('modal-toggle-user');
    if (modalToggleUser) {
        modalToggleUser.checked = false;
    }

    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/sw.js')
            .then(() => console.log('FIDEX: SW registered'))
            .catch(err => console.error('FIDEX: SW failed', err));
    }

    document.body.addEventListener('input', function (e) {
        if (e.target.id === 'cpf') {
            maskCPF(e.target);
        } else if (e.target.id === 'phone') {
            maskPhone(e.target);
        }
    });
});

function maskCPF(el) {
    let value = el.value.replace(/\D/g, '');
    if (value.length > 11) value = value.slice(0, 11);
    let formatted = value;
    if (value.length > 9) {
        formatted = value.replace(/(\d{3})(\d{3})(\d{3})(\d{1,2})/, '$1.$2.$3-$4');
    } else if (value.length > 6) {
        formatted = value.replace(/(\d{3})(\d{3})(\d{1,3})/, '$1.$2.$3');
    } else if (value.length > 3) {
        formatted = value.replace(/(\d{3})(\d{1,3})/, '$1.$2');
    }
    el.value = formatted;
}

function maskPhone(el) {
    let value = el.value.replace(/\D/g, '');
    if (value.length > 11) value = value.slice(0, 11);
    let formatted = value;
    if (value.length > 10) {
        formatted = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (value.length > 6) {
        formatted = value.replace(/(\d{2})(\d{4})(\d{1,4})/, '($1) $2-$3');
    } else if (value.length > 2) {
        formatted = value.replace(/(\d{2})(\d{1,4})/, '($1) $2');
    } else if (value.length > 0) {
        formatted = '(' + value;
    }
    el.value = formatted;
}

class OptimisticUI {
    constructor() {
        this.pendingKey = 'fidex_pending_actions';
        this.processQueue();
    }

    bind(formSelector, listId, type) {
        const form = document.querySelector(formSelector);
        if (!form) return;

        form.classList.add('optimistic-form');

        form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.handlesubmit(e, form, listId, type);
        });
    }

    handlesubmit(e, form, listId, type) {
        const formData = new FormData(form);
        const data = Object.fromEntries(formData.entries());

        form.querySelectorAll('select').forEach(select => {
            const option = select.options[select.selectedIndex];
            if (option) {
                // Extract base name (e.g., 'client' from 'client.id')
                const baseName = select.name.split('.')[0];
                data[baseName + 'Text'] = option.text;
            }
        });

        const tempId = 'temp-' + Date.now();
        data.tempId = tempId;
        data.timestamp = new Date().toISOString();

        this.renderItem(type, data, listId);

        const modalToggle = document.getElementById('modal-toggle');
        if (modalToggle) modalToggle.checked = false;

        form.reset();

        this.showToast('Salvando...');

        this.queueAction(form.action, data, tempId);
    }

    renderItem(type, data, listId) {
        const container = document.getElementById(listId) || document.querySelector('.tiles');
        if (!container) return;

        let html = '';
        if (type === 'client') {
            html = `
            <article class="tile optimistic-pending" id="${data.tempId}" style="opacity: 0.7; border-left: 4px solid var(--c-green-500);">
                <div class="tile-header">
                    <h3>${data.name}</h3>
                    <div class="customer-data">
                        <span>
                            <svg width="20" height="20" fill="#000000"><use xlink:href="#icon-phone"></use></svg>
                            <span class="cpf">${this.formatPhone(data.phone || '')}</span>
                        </span>
                        <span>
                            <svg width="20" height="20" fill="#000000"><use xlink:href="#icon-user"></use></svg>
                            <span>${this.formatCPF(data.cpf || '')}</span>
                        </span>
                    </div>
                </div>
                <div class="customer-stats">
                    <i>0 pontos</i>
                </div>
            </article>`;
        } else if (type === 'product') {
            // Calculate points from price (typically price value = points needed)
            const calculatedPoints = Math.floor(parseFloat(data.price || 0));
            html = `
            <article class="tile optimistic-pending" id="${data.tempId}" style="opacity: 0.7; border-left: 4px solid var(--c-product-500);">
                <div class="tile-header">
                    <div class="product-data">
                        <span>
                            <svg width="20" height="20" fill="#000000">
                                <use xlink:href="#icon-product"></use>
                            </svg>
                            <span>${data.quantity} itens restantes</span>
                        </span>
                    </div>
                    <h3>${data.name}</h3>
                </div>
                <div class="customer-stats">
                    <i>${calculatedPoints} pontos necessários</i>
                </div>
            </article>`;
        } else if (type === 'purchase') {
            const points = Math.floor((data.price || 0) * 5 / 100);
            const formattedPrice = parseFloat(data.price || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

            html = `
            <article class="tile optimistic-pending" id="${data.tempId}" style="opacity: 0.7; border-left: 4px solid var(--c-purple-500);">
                <div class="tile-header">
                    <div class="product-data" style="margin-bottom: 8px;">
                        <span>
                            <svg width="16" height="16" fill="#000000">
                                <use xlink:href="#icon-calendar"></use>
                            </svg>
                            <span>${new Date().toLocaleDateString('pt-BR')}</span>
                        </span>
                    </div>
                    <h3>${data.clientText || 'Cliente'}</h3>
                </div>
                <div class="customer-stats">
                    <i>${points} pontos</i>
                    <i>${formattedPrice}</i>
                </div>
            </article>`;
        } else if (type === 'prize') {
            html = `
            <article class="tile optimistic-pending" id="${data.tempId}" style="opacity: 0.7; border-left: 4px solid var(--c-prize-500);">
                <div class="tile-header">
                    <div class="customer-data">
                        <span>
                            <svg width="16" height="16" fill="#000000">
                                <use xlink:href="#icon-calendar"></use>
                            </svg>
                            <span>${new Date().toLocaleDateString('pt-BR')}</span>
                        </span>
                    </div>
                    <h3>${data.clientText || 'Cliente'}</h3>
                </div>
                <div class="customer-stats">
                    <i class="prize-pill">${data.productText || 'Produto'}</i>
                </div>
            </article>`;
        }

        if (html) {
            container.insertAdjacentHTML('afterbegin', html);
        }
    }

    queueAction(url, data, tempId) {
        const action = { url, data, tempId, type: 'POST' };
        let queue = JSON.parse(localStorage.getItem(this.pendingKey) || '[]');
        queue.push(action);
        localStorage.setItem(this.pendingKey, JSON.stringify(queue));
        this.processQueue();
    }

    async processQueue() {
        let queue = JSON.parse(localStorage.getItem(this.pendingKey) || '[]');
        if (queue.length === 0) return;

        const action = queue[0];

        try {
            const formData = new FormData();
            for (const key in action.data) {
                if (key !== 'tempId' && key !== 'timestamp' && !key.endsWith('Text')) {
                    formData.append(key, action.data[key]);
                }
            }

            const response = await fetch(action.url, {
                method: action.type,
                body: formData,
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });

            if (response.ok) {
                const el = document.getElementById(action.tempId);
                if (el) {
                    el.style.opacity = '1';
                    el.classList.remove('optimistic-pending');
                }
                queue.shift();
                localStorage.setItem(this.pendingKey, JSON.stringify(queue));
                this.processQueue();
            } else {
                console.error('Sync failed', response);
                if (response.status >= 400 && response.status < 500) {
                    queue.shift();
                    const el = document.getElementById(action.tempId);
                    if (el) el.remove();
                    this.showToast('Erro ao salvar item. Verifique os dados.');
                } else {
                    setTimeout(() => this.processQueue(), 5000);
                }
                localStorage.setItem(this.pendingKey, JSON.stringify(queue));
            }
        } catch (error) {
            console.error('Network error', error);
            setTimeout(() => this.processQueue(), 5000);
        }
    }

    showToast(msg) {
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.textContent = msg;
        toast.style.cssText = `
            position: fixed; bottom: 20px; right: 20px; 
            background: #252525; color: #fff; padding: 12px 24px; 
            border-radius: 8px; z-index: 9999; animation: slideIn 0.3s ease;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15); font-family: 'Inter', sans-serif;
            border: 1px solid #333;
        `;
        document.body.appendChild(toast);
        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }

    formatCPF(v) {
        v = v.replace(/\D/g, "");
        if (v.length <= 11) v = v.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
        return v;
    }
    formatPhone(v) {
        v = v.replace(/\D/g, "");
        v = v.replace(/^(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
        return v;
    }
}

const optimistic = new OptimisticUI();
document.addEventListener("DOMContentLoaded", function () {

    if (window.location.pathname.includes('/clientes')) {
        optimistic.bind('.form', 'clientes', 'client');
    }

    if (window.location.pathname.includes('/produtos')) {
        optimistic.bind('.form', 'produtos', 'product');
    }

    if (window.location.pathname.includes('/compras')) {
        optimistic.bind('.form', 'compras', 'purchase');
    }

    // Prizes disabled from optimistic UI due to server-side validation (points check)
    // if (window.location.pathname.includes('/premios')) {
    //     optimistic.bind('.form', 'premios', 'prize');
    // }
});
