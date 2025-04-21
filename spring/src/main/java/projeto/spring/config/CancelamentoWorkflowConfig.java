package projeto.spring.config;

import projeto.spring.Model.CancelamentoMatriculaEstado;
import projeto.spring.Model.CancelamentoMatriculaEvento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class CancelamentoWorkflowConfig extends StateMachineConfigurerAdapter<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> {

    private static final Logger logger = LoggerFactory.getLogger(CancelamentoWorkflowConfig.class);

    @Override
    public void configure(StateMachineStateConfigurer<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> states) throws Exception {
        states.withStates()
                .initial(CancelamentoMatriculaEstado.SOLICITADO, initialAction())
                .states(EnumSet.allOf(CancelamentoMatriculaEstado.class))
                .end(CancelamentoMatriculaEstado.APROVADO_SECRETARIA);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> transitions) throws Exception {
        transitions.withExternal()
                .source(CancelamentoMatriculaEstado.SOLICITADO)
                .target(CancelamentoMatriculaEstado.APROVADO_COORDENADOR)
                .event(CancelamentoMatriculaEvento.APROVAR_COORDENADOR)
                .and()
                .withExternal()
                .source(CancelamentoMatriculaEstado.APROVADO_COORDENADOR)
                .target(CancelamentoMatriculaEstado.APROVADO_SECRETARIA)
                .event(CancelamentoMatriculaEvento.APROVAR_SECRETARIA)
                .action(ctx -> {
                    logger.info("Variáveis estendidas: " + ctx.getStateMachine().getExtendedState().getVariables());
                });
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener())
                .machineId("cancelamento-matricula");
    }

    @Bean
    public Action<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> initialAction() {
        return context -> logger.info("Iniciado o WF Cancelamento de Matrícula");
    }

    @Bean
    public StateMachineListener<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> from,
                                     State<CancelamentoMatriculaEstado, CancelamentoMatriculaEvento> to) {
                logger.info("Estado alterado para: " + to.getId());
            }
        };
    }
}

