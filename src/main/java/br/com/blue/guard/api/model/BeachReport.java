package br.com.blue.guard.api.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "blue_guard_beach_report")
public class BeachReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beach_report_id")
    private Long id;
    
    @NotNull @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String location;

    @NotBlank
    private String condition;

    @NotBlank
    private String description;
    
    @CreationTimestamp
    private Instant timestamp;

    private String imageUrl;

    public void setUser(User user) {
        this.user = user;
    }
}
