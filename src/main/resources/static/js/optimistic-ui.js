document.addEventListener('DOMContentLoaded', function () {
    const forms = document.querySelectorAll('form[data-optimistic]');

    forms.forEach(form => {
        form.addEventListener('submit', function (e) {
            e.preventDefault();

            const formData = new FormData(form);
            const data = Object.fromEntries(formData.entries());

            // Capture labels from select elements
            form.querySelectorAll('select').forEach(select => {
                const name = select.name;
                const label = select.options[select.selectedIndex].text;
                data[name + 'Label'] = label;
                // Special case for clientId -> clientLabel
                if (name === 'clientId') {
                    data['clientLabel'] = label;
                }
                // Special case for productId -> productLabel
                if (name === 'productId') {
                    data['productLabel'] = label;
                }
            });

            const listId = form.getAttribute('data-list');
            const listContainer = document.getElementById(listId);
            const modalToggle = document.getElementById(form.getAttribute('data-modal-toggle'));

            // Create optimistic item
            const optimisticItem = createOptimisticItem(data, listId);
            if (optimisticItem && listContainer) {
                // Prepend to the list
                listContainer.insertBefore(optimisticItem, listContainer.firstChild);
                optimisticItem.style.opacity = '0.5';
                optimisticItem.classList.add('optimistic-pending');
            }

            // Close modal
            if (modalToggle) {
                modalToggle.checked = false;
            }

            // Send request
            fetch(form.action, {
                method: 'POST',
                body: new URLSearchParams(formData),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(response => {
                    if (response.ok) {
                        // Refresh the container to get the real ID and layout from server
                        // This ensures full fidelity after the quick optimistic strike
                        fetch(window.location.href)
                            .then(res => res.text())
                            .then(html => {
                                const parser = new DOMParser();
                                const doc = parser.parseFromString(html, 'text/html');
                                const newList = doc.getElementById(listId);
                                if (newList && listContainer) {
                                    listContainer.innerHTML = newList.innerHTML;
                                }
                            });
                    } else {
                        alert('Erro ao salvar. Por favor, tente novamente.');
                        if (optimisticItem) optimisticItem.remove();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Erro de conexão.');
                    if (optimisticItem) optimisticItem.remove();
                });

            form.reset();
        });
    });
});

const article = document.createElement('article');
article.className = 'tile';

if (type === 'clientes') {
    article.innerHTML = `
            <div class="tile-header">
                <h3>${data.name || 'Novo Cliente'}</h3>
                <div class="customer-data">
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#000000" viewBox="0 0 256 256">
                            <path d="M187.58,144.84l-32-16a8,8,0,0,0-8,.5l-14.69,9.8a40.55,40.55,0,0,1-16-16l9.8-14.69a8,8,0,0,0,.5-8l-16-32A8,8,0,0,0,104,64a40,40,0,0,0-40,40,88.1,88.1,0,0,0,88,88,40,40,0,0,0,40-40A8,8,0,0,0,187.58,144.84ZM152,176a72.08,72.08,0,0,1-72-72A24,24,0,0,1,99.29,80.46l11.48,23L101,118a8,8,0,0,0-.73,7.51,56.47,56.47,0,0,0,30.15,30.15A8,8,0,0,0,138,155l14.61-9.74,23,11.48A24,24,0,0,1,152,176ZM128,24A104,104,0,0,0,36.18,176.88L24.83,210.93a16,16,0,0,0,20.24,20.24l34.05-11.35A104,104,0,1,0,128,24Zm0,192a87.87,87.87,0,0,1-44.06-11.81,8,8,0,0,0-6.54-.67L40,216,52.47,178.6a8,8,0,0,0-.66-6.54A88,88,0,1,1,128,216Z"></path>
                        </svg>
                        <span class="cpf">${data.phone || ''}</span>
                    </span>
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#000000" viewBox="0 0 256 256">
                            <path d="M75.19,198.4a8,8,0,0,0,11.21-1.6,52,52,0,0,1,83.2,0,8,8,0,1,0,12.8-9.6A67.88,67.88,0,0,0,155,165.51a40,40,0,1,0-53.94,0A67.88,67.88,0,0,0,73.6,187.2,8,8,0,0,0,75.19,198.4ZM128,112a24,24,0,1,1-24,24A24,24,0,0,1,128,112Zm72-88H56A16,16,0,0,0,40,40V216a16,16,0,0,0,16,16H200a16,16,0,0,0,16-16V40A16,16,0,0,0,200,24Zm0,192H56V40H200ZM88,64a8,8,0,0,1,8-8h64a8,8,0,0,1,0,16H96A8,8,0,0,1,88,64Z"></path>
                        </svg>
                        <span>${data.cpf || ''}</span>
                    </span>
                </div>
            </div>
            <div class="customer-stats">
                <i>0 pontos</i>
            </div>
        `;
} else if (type === 'produtos') {
    article.innerHTML = `
            <div class="tile-header">
                <div class="product-data">
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#000000" viewBox="0 0 256 256"><path d="M223.16,68.42l-16-32A8,8,0,0,0,200,32H56a8,8,0,0,0-7.16,4.42l-16,32A8.08,8.08,0,0,0,32,72V208a16,16,0,0,0,16,16H208a16,16,0,0,0,16-16V72A8.08,8.08,0,0,0,223.16,68.42ZM60.94,48H195.06l8,16H52.94ZM208,208H48V80H208V208Zm-42.34-61.66a8,8,0,0,1,0,11.32l-32,32a8,8,0,0,1-11.32,0l-32-32a8,8,0,0,1,11.32-11.32L120,164.69V104a8,8,0,0,1,16,0v60.69l18.34-18.35A8,8,0,0,1,165.66,146.34Z"></path></svg>
                        <span>${data.quantity || '0'} itens restantes</span>
                    </span>
                </div>
                <h3>${data.name || 'Novo Produto'}</h3>
            </div>
            <div class="customer-stats">
                <i><span>${data.points || '0'}</span> pontos necessários</i>
            </div>
        `;
} else if (type === 'premios') {
    const dateVal = data.date ? new Date(data.date).toLocaleDateString('pt-BR') : new Date().toLocaleDateString('pt-BR');
    article.innerHTML = `
            <div class="tile-header">
                <div class="customer-data" style="margin-bottom: 8px;">
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="#000000" viewBox="0 0 256 256">
                            <path d="M208,32H184V24a8,8,0,0,0-16,0v8H88V24a8,8,0,0,0-16,0v8H48A16,16,0,0,0,32,48V208a16,16,0,0,0,16,16H208a16,16,0,0,0,16-16V48A16,16,0,0,0,208,32ZM72,48v8a8,8,0,0,0,16,0V48h80v8a8,8,0,0,0,16,0V48h24V80H48V48ZM208,208H48V96H208V208Z"></path>
                        </svg>
                        <span>${dateVal}</span>
                    </span>
                </div>
                <h3>${data.clientLabel || ''}</h3>
            </div>
            <div class="customer-stats">
                <i class="prize-pill">${data.productLabel || ''}</i>
            </div>
        `;
} else if (type === 'compras') {
    const price = parseFloat(data.price || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    const dateVal = data.date ? new Date(data.date).toLocaleDateString('pt-BR') : new Date().toLocaleDateString('pt-BR');

    article.innerHTML = `
            <div class="tile-header">
                <div class="product-data">
                    <span>
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#000000" viewBox="0 0 256 256"><path d="M208,32H184V24a8,8,0,0,0-16,0v8H88V24a8,8,0,0,0-16,0v8H48A16,16,0,0,0,32,48V208a16,16,0,0,0,16,16H208a16,16,0,0,0,16-16V48A16,16,0,0,0,208,32ZM72,48v8a8,8,0,0,0,16,0V48h80v8a8,8,0,0,0,16,0V48h24V80H48V48ZM208,208H48V96H208V208Zm-38.34-85.66a8,8,0,0,1,0,11.32l-48,48a8,8,0,0,1-11.32,0l-24-24a8,8,0,0,1,11.32-11.32L116,164.69l42.34-42.35A8,8,0,0,1,169.66,122.34Z"></path></svg>
                        <span>${dateVal}</span>
                    </span>
                </div>
                <h3>${data.clientLabel || ''}</h3>
            </div>
            <div class="customer-stats">
                <i><span>${data.points || '0'}</span> pontos</i>
                <i><span>${price}</span></i>
            </div>
        `;
}

return article;
}
