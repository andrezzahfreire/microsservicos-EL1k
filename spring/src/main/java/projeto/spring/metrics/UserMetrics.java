package projeto.spring.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import projeto.spring.Repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private UsuarioRepository userRepository;

    @jakarta.annotation.PostConstruct
    public void registerUserCountMetric() {
        Gauge.builder("app.users.total", userRepository, repo -> repo.count())
                .description("Total de usuários cadastrados na plataforma")
                .register(meterRegistry);
    }
}
