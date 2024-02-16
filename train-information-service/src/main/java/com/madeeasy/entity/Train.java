package com.madeeasy.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Train {

    @Id
    @Column(unique = true, length = 30)
    private String trainId;
    @Column(unique = true, length = 30)
    private String trainName;
    @Column(unique = true, length = 30)
    private String trainNumber;
    @Column(nullable = false,length = 30)
    private String startingStation;     // Starting point of the train route
    @Column(nullable = false, length = 30)
    private String endingStation;       // Ending point or destination of the train route
    private String trainDescription; // Description of the train

    @OneToMany(
            mappedBy = "train",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<TrainClass> classes; // Different classes available on the train

    @OneToMany(
            mappedBy = "train",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<TrainStop> stops;    // Stops along the train route


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Train train = (Train) o;
        return getTrainId() != null && Objects.equals(getTrainId(), train.getTrainId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", trainName='" + trainName + '\'' +
                ", trainNumber='" + trainNumber + '\'' +
                ", startingStation='" + startingStation + '\'' +
                ", endingStation='" + endingStation + '\'' +
                ", trainDescription='" + trainDescription + '\'' +
                ", classes=" + classes +
                ", stops=" + stops +
                '}';
    }
}
