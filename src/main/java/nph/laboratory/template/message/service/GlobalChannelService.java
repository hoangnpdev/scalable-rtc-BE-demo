package nph.laboratory.template.message.service;

import nph.laboratory.template.message.entity.GlobalChannel;
import nph.laboratory.template.message.repository.GlobalChannelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalChannelService {

    private final GlobalChannelRepository globalChannelRepository;

    public GlobalChannelService(GlobalChannelRepository globalChannelRepository) {
        this.globalChannelRepository = globalChannelRepository;
    }

    public List<GlobalChannel> getGlobalChannels() {
        return globalChannelRepository.findAll();
    }

}
