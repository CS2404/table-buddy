package com.cs2404.tablebuddy.store.entity;


import com.cs2404.tablebuddy.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Column(name = "zipcode", length = 5, nullable = false)
    private String zipcode;

    @Column(name = "country", length = 50, nullable = false)
    private String country;

    @Column(name = "state", length = 10, nullable = false)
    private String state;

    @Column(name = "city", length = 10, nullable = false)
    private String city;

    @Column(name = "district", length = 10)
    private String district;

    @Column(name = "addressLine1", length = 30, nullable = false)
    private String addressLine1;

    @Column(name = "addressLine2", length = 30)
    private String addressLine2;

    @Builder
    public AddressEntity(Long id,
                         StoreEntity store,
                         String zipcode,
                         String country,
                         String state,
                         String city,
                         String district,
                         String addressLine1,
                         String addressLine2) {
        this.id = id;
        this.store = store;
        this.zipcode = zipcode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.district = district;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
    }
}

