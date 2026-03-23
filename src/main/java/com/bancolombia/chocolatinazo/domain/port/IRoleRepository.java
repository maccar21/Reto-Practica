package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.enums.RoleName;
import com.bancolombia.chocolatinazo.domain.model.Role;

import java.util.Optional;

public interface IRoleRepository {
    Optional<Role> findByName(RoleName name);
}
