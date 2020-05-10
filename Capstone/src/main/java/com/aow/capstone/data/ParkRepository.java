package com.aow.capstone.data;

import com.aow.capstone.entities.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alex White
 */
@Repository
public interface ParkRepository extends JpaRepository<Park, String> {

}
