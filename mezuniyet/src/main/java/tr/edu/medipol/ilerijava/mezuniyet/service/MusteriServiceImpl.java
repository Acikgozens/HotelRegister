package tr.edu.medipol.ilerijava.mezuniyet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.edu.medipol.ilerijava.mezuniyet.data.MusteriDTO;
import tr.edu.medipol.ilerijava.mezuniyet.entity.Musteri;
import tr.edu.medipol.ilerijava.mezuniyet.repo.MusteriRepository;

import java.util.List;

@Service
public class MusteriServiceImpl implements MusteriService {

    @Autowired
    MusteriRepository musteriRepository;

    public Musteri musteriEkle(MusteriDTO musteriDTO){
        Musteri musteri = new Musteri();
        musteri.setTcKimlikNo(musteriDTO.getTcKimlikNo());
        musteri.setAd(musteriDTO.getAd());
        musteri.setSoyad(musteriDTO.getSoyad());
        musteri.setGirisTarihi(musteriDTO.getGirisTarihi());
        musteri.setDogumTarihi(musteriDTO.getDogumTarihi());
        musteri.setTelefon(musteriDTO.getTelefon());
        musteri.setEposta(musteriDTO.getEposta());
        musteriRepository.save(musteri);
        return musteri;

    }

    public List<Musteri> musterileriListele(){
        return musteriRepository.findAll();
    }

    public Musteri musteriGetir(Long id){
        return musteriRepository.findById(id).get();
    }

    public String musteriSil(Long id){
        musteriRepository.deleteById(id);
        return id + " nolu kisi silindi.";
    }

    public Musteri musteriGuncelle(Long id, MusteriDTO musteriDTO){
        Musteri musteri = musteriGetir(id);
        musteri.setTcKimlikNo(musteriDTO.getTcKimlikNo());
        musteri.setAd(musteriDTO.getAd());
        musteri.setSoyad(musteriDTO.getSoyad());
        musteri.setGirisTarihi(musteriDTO.getGirisTarihi());
        musteri.setDogumTarihi(musteriDTO.getDogumTarihi());
        musteri.setTelefon(musteriDTO.getTelefon());
        musteri.setEposta(musteriDTO.getEposta());
        musteriRepository.save(musteri);
        return musteri;
    }
}
