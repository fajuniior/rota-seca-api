package br.com.fiap.rotaseca.bo;

import br.com.fiap.rotaseca.dao.UsuarioDAO;
import br.com.fiap.rotaseca.entities.Usuario;
import br.com.fiap.rotaseca.excecoes.ExcecoesConexao;
import br.com.fiap.rotaseca.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.rotaseca.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Regras de negócio de Usuario.
 */
public class UsuarioBo {

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Selecionar
    public ArrayList<Usuario> selecionarBo() throws SQLException, ExcecoesConexao {
        return (ArrayList<Usuario>) usuarioDAO.selecionar();
    }

    // Buscar por ID
    public Usuario buscarPorIdBo(int id) throws SQLException, ExcecoesConexao {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Usuario " + id + " não encontrado");
        }
        return usuario;
    }

    // Inserir
    public Usuario inserirBo(Usuario usuario) throws SQLException, ExcecoesConexao {
        Validacao.corpo(usuario);
        // Regra de negócio
        validar(usuario);
        if (usuario.getIdUsuario() <= 0) {
            usuario.setIdUsuario(usuarioDAO.proximoId());
        }
        usuarioDAO.inserir(usuario);
        return usuario;
    }

    // Atualizar
    public Usuario atualizarBo(int id, Usuario usuario) throws SQLException, ExcecoesConexao {
        Validacao.corpo(usuario);
        usuario.setIdUsuario(id);
        // Regra de negócio
        validar(usuario);
        if (!usuarioDAO.atualizar(usuario)) {
            throw new RecursoNaoEncontradoException("Usuario " + id + " não encontrado");
        }
        return usuario;
    }

    // Deletar
    public void deletarBo(int id) throws SQLException, ExcecoesConexao {
        if (!usuarioDAO.deletar(id)) {
            throw new RecursoNaoEncontradoException("Usuario " + id + " não encontrado");
        }
    }

    // Validações antes de gravar
    private void validar(Usuario usuario) throws SQLException, ExcecoesConexao {
        Validacao.obrigatorio(usuario.getNomeExibicao(), "nomeExibicao");
        Validacao.obrigatorio(usuario.getTelefone(), "telefone");
        if (Validacao.vazio(usuario.getTipoPerfil())) usuario.setTipoPerfil("CIDADAO");
        Validacao.dominio(usuario.getTipoPerfil(), "tipoPerfil", "CIDADAO", "AGENTE", "ADMIN");
        if (Validacao.vazio(usuario.getStAtivo())) usuario.setStAtivo("S");
        Validacao.dominio(usuario.getStAtivo(), "stAtivo", "S", "N");
        if (usuario.getDtCadastro() == null) usuario.setDtCadastro(Relogio.agora());
    }

    // Garante que o usuário existe e tem perfil de agente da Defesa Civil
    public Usuario exigirAgente(int idUsuario) throws SQLException, ExcecoesConexao {
        Usuario usuario = buscarPorIdBo(idUsuario);
        if (!"AGENTE".equals(usuario.getTipoPerfil())) {
            throw new RegraNegocioException("O usuário " + idUsuario + " não é agente da Defesa Civil");
        }
        return usuario;
    }

    // Avisos dos caminhos salvos do usuário (US02)
    public java.util.List<br.com.fiap.rotaseca.dto.AvisoCaminho> avisosDosCaminhosBo(int idUsuario)
            throws SQLException, ExcecoesConexao {
        buscarPorIdBo(idUsuario);
        return new br.com.fiap.rotaseca.dao.AvisoCaminhoDAO().selecionarPorUsuario(idUsuario, Relogio.agora());
    }
}
