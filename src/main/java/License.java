import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

@Getter
@Setter
@ToString
//@Data
public class License implements Comparable<License> {
    private int id;
    private String lnu;
    private String lt;
    private String nu;
    private String pi;
    private String ad;
    private String ot;
    private String nl;
    private LocalDate dn;
    private String dk;
    private String kodDila;
    private String type;
    private String d_nak;
    private String n_nak;
    private String t_nak;
    private String activity;
    private String k_dree;

    public String toStringWithSeparator(String separator) {
        return
                lnu + separator +
                lt + separator +
                nu + separator +
                pi + separator +
                ad + separator +
                ot + separator +
                nl + separator +
                dn.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + separator +
                dk + separator +
                kodDila + separator +
                type + separator +
                d_nak + separator +
                n_nak + separator +
                t_nak + separator +
                activity + separator +
                k_dree + separator;
    }

    @Override
    public String toString() {
        return "License{" +
                "lt='" + lt + '\'' +
                ", nl='" + nl + '\'' +
                ", dn='" + dn + '\'' +
                ", type='" + type + '\'' +
                ", k_dree='" + k_dree + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        License license = (License) o;

        if (!Objects.equals(lt, license.lt)) return false;
        return Objects.equals(nl, license.nl);
//        return Objects.equals(dn, license.dn);
    }


    @Override
    public int hashCode() {
        int result = lt != null ? lt.hashCode() : 0;
        result = 31 * result + (nl != null ? nl.hashCode() : 0);
//        result = 31 * result + (dn != null ? dn.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(License o) {
        LocalDate first = this.dn;
        LocalDate second = o.dn;
//        if (Objects.nonNull(second) && Objects.nonNull(first)) {
//            System.out.println("NPE: " + first + ": second: " + second);
//            return first.compareTo(second);
//        }
//        return 0;
        return first.compareTo(second);

//        return Comparator.comparing(License::getNl)
//                .thenComparing(License::getDn)
//                .compare(this, o);
    }
}
