package nph.laboratory.template.message.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GlobalChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer channelId;

    @Column(unique = true)
    private String channelName;

}
