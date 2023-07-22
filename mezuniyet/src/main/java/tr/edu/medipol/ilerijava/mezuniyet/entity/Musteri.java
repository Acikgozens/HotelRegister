package tr.edu.medipol.ilerijava.mezuniyet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Musteri {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tcKimlikNo;
    private String ad;
    private String soyad;
    private LocalDate girisTarihi;
    private LocalDate dogumTarihi;
    private String telefon;
    private String eposta;
}
