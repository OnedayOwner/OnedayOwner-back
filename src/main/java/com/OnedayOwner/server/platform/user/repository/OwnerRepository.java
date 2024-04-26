package com.OnedayOwner.server.platform.user.repository;


import com.OnedayOwner.server.platform.user.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner,Long> {


}
