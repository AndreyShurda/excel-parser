import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LicenseFilter extends License {
    private LocalDate dnCorrect;
}
