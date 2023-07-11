package store.cookshoong.www.cookshoongspringbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health Test.
 *
 * @author seungyeon
 * @since 2023.07.11
 */
@RestController
@RequestMapping(path = "/health")
@RequiredArgsConstructor
public class Test {
    private final ApplicationHealthIndicator healthIndicator;

    @PostMapping("/up")
    public void up(){
        final Health up = Health.up().build();
        healthIndicator.setHealth(up);
    }

    @PostMapping("/down")
    public void down(){
        final Health down = Health.down().build();
        healthIndicator.setHealth(down);
    }
}
