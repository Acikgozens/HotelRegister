package tr.edu.medipol.ilerijava.mezuniyet.service;

import tr.edu.medipol.ilerijava.mezuniyet.data.MusteriDTO;
import tr.edu.medipol.ilerijava.mezuniyet.entity.Musteri;

import java.util.List;

public interface MusteriService {
    Musteri musteriEkle(MusteriDTO musteriDTO);

    List<Musteri> musterileriListele();

    Musteri musteriGetir(Long id);

    String musteriSil(Long id);

    Musteri musteriGuncelle(Long id, MusteriDTO musteriDTO);
}
