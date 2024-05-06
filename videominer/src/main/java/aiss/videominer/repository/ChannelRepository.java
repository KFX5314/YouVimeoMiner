package aiss.videominer.repository;

import aiss.videominer.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, String> {

}
