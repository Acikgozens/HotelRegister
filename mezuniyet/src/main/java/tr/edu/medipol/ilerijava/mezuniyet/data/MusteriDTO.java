package tr.edu.medipol.ilerijava.mezuniyet.data;

import lombok.Data;


import java.time.LocalDate;
import java.util.Date;

@Data
public class MusteriDTO {

    private String tcKimlikNo;
    private String ad;
    private String soyad;
    private LocalDate girisTarihi;
    private LocalDate dogumTarihi;
    private String telefon;
    private String eposta;

}
