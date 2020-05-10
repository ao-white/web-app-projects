package com.aow.capstone.data;

import com.aow.capstone.entities.ParkVisit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alex White
 */
@Repository
public interface ParkVisitRepository extends JpaRepository<ParkVisit, Integer> {
    List<ParkVisit> findByUserAccountUserId(int id);
}
