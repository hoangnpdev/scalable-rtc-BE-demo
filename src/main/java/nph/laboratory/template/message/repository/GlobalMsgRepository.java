package nph.laboratory.template.message.repository;

import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.entity.GlobalMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalMsgRepository extends JpaRepository<GlobalMsg, Long> {

    List<GlobalMsg> findGlobalMsgByChannelOrderByTimestampAsc(GlobalChannel channel);
}
