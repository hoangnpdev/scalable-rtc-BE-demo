package nph.laboratory.template.message.repository;

import nph.laboratory.template.message.entity.GlobalChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalChannelRepository extends JpaRepository<GlobalChannel, Long> {
    GlobalChannel findGlobalChannelByChannelName(String channelName);
}
