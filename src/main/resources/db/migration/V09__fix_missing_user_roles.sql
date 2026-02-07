-- Fix: restore ROLE_USUARIO for all active users who lost their roles
-- This was caused by PerfilController.salvarCashback() saving the user
-- without roles, which triggered UsuarioRepository.save() to delete
-- all entries from usuario_papel.
INSERT INTO usuario_papel (codigo_usuario, codigo_papel)
SELECT u.codigo, p.codigo
FROM usuario u
CROSS JOIN papel p
WHERE u.ativo = true
  AND p.nome = 'ROLE_USUARIO'
  AND NOT EXISTS (
    SELECT 1 FROM usuario_papel up WHERE up.codigo_usuario = u.codigo
  );
