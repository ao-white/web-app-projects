package com.aow.capstone.data;

import com.aow.capstone.entities.Picture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alex White
 */
@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    List<Picture> findByParkVisitVisitId(int id);
}
