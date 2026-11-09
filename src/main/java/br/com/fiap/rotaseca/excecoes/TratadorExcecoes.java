package br.com.fiap.rotaseca.excecoes;

import br.com.fiap.rotaseca.bo.Relogio;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;

/**
 * Camada de tratamento de exceções: transforma cada erro em uma resposta HTTP
 * com status correto e mensagem em português, em vez de um erro 500 genérico.
 */
@RestControllerAdvice
public class TratadorExcecoes {

    private static final Logger LOG = LoggerFactory.getLogger(TratadorExcecoes.class);

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem, HttpServletRequest req) {
        ErroResposta erro = new ErroResposta(Relogio.agora(), status.value(), status.getReasonPhrase(),
                mensagem, req.getRequestURI());
        return ResponseEntity.status(status).body(erro);
    }

    // 404 - registro não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest req) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    // 400 - regra de negócio
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> regraNegocio(RegraNegocioException e, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    // 400 - JSON inválido ou campo com tipo errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> jsonInvalido(HttpMessageNotReadableException e, HttpServletRequest req) {
        LOG.warn("JSON inválido: {}", e.getMessage());
        return resposta(HttpStatus.BAD_REQUEST, "JSON inválido ou campo com tipo incorreto", req);
    }

    // 400 - parâmetro da URL com tipo errado (ex.: /bairros/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoParametro(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro '" + e.getName() + "' com valor inválido", req);
    }

    // 400 - parâmetro obrigatório ausente
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> parametroAusente(MissingServletRequestParameterException e, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente: " + e.getParameterName(), req);
    }

    // 404 - rota inexistente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> rotaInexistente(NoResourceFoundException e, HttpServletRequest req) {
        return resposta(HttpStatus.NOT_FOUND, "Rota não encontrada", req);
    }

    // 403 - operação exclusiva de agente sem credencial válida
    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResposta> acessoNegado(AcessoNegadoException e, HttpServletRequest req) {
        return resposta(HttpStatus.FORBIDDEN, e.getMessage(), req);
    }

    // 405 - método HTTP não permitido nesta rota (ex.: PUT em /confirmacoes)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoPermitido(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        return resposta(HttpStatus.METHOD_NOT_ALLOWED, "Método " + e.getMethod() + " não é permitido nesta rota", req);
    }

    // 503 - banco fora do ar ou credenciais erradas
    @ExceptionHandler(ExcecoesConexao.class)
    public ResponseEntity<ErroResposta> conexao(ExcecoesConexao e, HttpServletRequest req) {
        LOG.error("Falha de conexão com o banco", e);
        return resposta(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), req);
    }

    // Erros do banco: traduz as violações de constraint para mensagens claras
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErroResposta> banco(SQLException e, HttpServletRequest req) {
        int codigo = e.getErrorCode();
        // Códigos Oracle (e equivalentes do H2, usado nos testes locais)
        if (codigo == 1 || codigo == 23505) {
            return resposta(HttpStatus.CONFLICT, "Registro duplicado: já existe um registro com estes dados", req);
        }
        if (codigo == 2291 || codigo == 23506) {
            return resposta(HttpStatus.BAD_REQUEST, "Referência inválida: um dos IDs informados não existe", req);
        }
        if (codigo == 2292 || codigo == 23503) {
            return resposta(HttpStatus.CONFLICT, "Não é possível excluir: existem registros que dependem deste", req);
        }
        if (codigo == 2290 || codigo == 23513) {
            return resposta(HttpStatus.BAD_REQUEST, "Valor fora das regras do banco de dados", req);
        }
        if (codigo == 1400 || codigo == 23502) {
            return resposta(HttpStatus.BAD_REQUEST, "Campo obrigatório não informado", req);
        }
        if (codigo == 12899 || codigo == 22001) {
            return resposta(HttpStatus.BAD_REQUEST, "Texto maior que o tamanho permitido", req);
        }
        LOG.error("Erro de banco não tratado", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados", req);
    }

    // 500 - qualquer outro erro
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> geral(Exception e, HttpServletRequest req) {
        LOG.error("Erro inesperado", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado", req);
    }
}
