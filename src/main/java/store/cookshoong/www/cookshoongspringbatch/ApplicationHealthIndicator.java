package store.cookshoong.www.cookshoongspringbatch;

import java.util.concurrent.atomic.AtomicReference;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Health setting.
 *
 * @author seungyeon
 * @since 2023.07.11
 */
@Component
public class ApplicationHealthIndicator implements HealthIndicator {
    private final AtomicReference<Health> healthRefer = new AtomicReference<>(Health.down().build());

    @Override
    public Health health() {
        return healthRefer.get();
    }

    public void setHealth(Health health){
        this.healthRefer.set(health);
    }
}
