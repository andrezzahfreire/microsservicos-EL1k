package projeto.spring.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import projeto.spring.Model.CancelamentoMatriculaEstado;
import projeto.spring.Model.CancelamentoMatriculaEvento;
import reactor.core.publisher.Mono;

@Component
public class CancelamentoWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(CancelamentoWorkflow.class);

    @Autowired
    private StateMachineFactory<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> factory;

    private StateMachine<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> maquina;

    public CancelamentoMatriculaEstado solicitar() {
        this.maquina = factory.getStateMachine();
        maquina.startReactively().block();
        logger.info("Solicitação iniciada: Estado = {}", maquina.getState().getId());
        return maquina.getState().getId();
    }

    public CancelamentoMatriculaEstado aprovarCoordenador() {
        maquina.sendEvent(Mono.just(MessageBuilder.withPayload(CancelamentoMatriculaEvento.APROVAR_COORDENADOR).build())).blockFirst();
        logger.info("Aprovado pelo coordenador: Estado = {}", maquina.getState().getId());
        return maquina.getState().getId();
    }

    public CancelamentoMatriculaEstado aprovarSecretaria() {
        maquina.getExtendedState().getVariables().put("ID_ALUNO", "X");
        maquina.sendEvent(Mono.just(MessageBuilder.withPayload(CancelamentoMatriculaEvento.APROVAR_SECRETARIA).build())).blockFirst();
        maquina.stopReactively().block();
        logger.info("Aprovado pela secretaria: Estado = {}", maquina.getState().getId());
        return maquina.getState().getId();
    }
}