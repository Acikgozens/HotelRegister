package tr.edu.medipol.ilerijava.mezuniyet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.edu.medipol.ilerijava.mezuniyet.entity.Musteri;

@Repository
public interface MusteriRepository extends JpaRepository<Musteri, Long> {


}