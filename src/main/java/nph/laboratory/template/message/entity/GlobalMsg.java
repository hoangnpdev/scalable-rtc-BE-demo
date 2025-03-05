package nph.laboratory.template.message.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class GlobalMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer msgId;

    private String accountName;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "global_channel_id",
            foreignKey = @ForeignKey(
                    name = "fk_channel_msg",
                    value = ConstraintMode.NO_CONSTRAINT
            )
    )
    private GlobalChannel channel;
}
