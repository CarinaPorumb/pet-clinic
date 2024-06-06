//package com.PetClinic.service.impl;
//
//import com.PetClinic.model.OwnerDTO;
//import com.PetClinic.service.OwnerService;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class OwnerServiceMapImpl implements OwnerService {
//
//    private final Map<UUID, OwnerDTO> ownerDTOMap;
//
//    public OwnerServiceMapImpl() {
//        this.ownerDTOMap = new HashMap<>();
//        OwnerDTO owner1 = OwnerDTO.builder()
//                .id(UUID.randomUUID())
//                .name("John")
//                .address("Paris")
//                .telephone("01234")
//                .build();
//
//        OwnerDTO owner2 = OwnerDTO.builder()
//                .id(UUID.randomUUID())
//                .name("Helen")
//                .address("London")
//                .telephone("123456")
//                .build();
//
//        OwnerDTO owner3 = OwnerDTO.builder()
//                .id(UUID.randomUUID())
//                .name("Maria")
//                .address("Lisbon")
//                .telephone("23456")
//                .build();
//
//        ownerDTOMap.put(owner1.getId(), owner1);
//        ownerDTOMap.put(owner2.getId(), owner2);
//        ownerDTOMap.put(owner3.getId(), owner3);
//    }
//
//    @Override
//    public Page<OwnerDTO> listOwners(int pageNumber, int pageSize) {
//        return null;
//    }
//
//
//    @Override
//    public Optional<OwnerDTO> getOwnerById(UUID id) {
//        return Optional.of(ownerDTOMap.get(id));
//    }
//
//    @Override
//    public OwnerDTO saveNewOwner(OwnerDTO ownerDTO) {
//        OwnerDTO savedOwnerDTO = OwnerDTO.builder()
//                .id(UUID.randomUUID())
//                .name(ownerDTO.getName())
//                .address(ownerDTO.getAddress())
//                .telephone(ownerDTO.getTelephone())
//                .build();
//        ownerDTOMap.put(savedOwnerDTO.getId(), savedOwnerDTO);
//        return savedOwnerDTO;
//    }
//
//    @Override
//    public Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO) {
//        com.PetClinic.model.OwnerDTO ownerDTOExisting = ownerDTOMap.get(id);
//        ownerDTOExisting.setName(ownerDTO.getName());
//        ownerDTOExisting.setAddress(ownerDTO.getAddress());
//        ownerDTOExisting.setTelephone(ownerDTO.getTelephone());
//        return Optional.of(ownerDTOExisting);
//    }
//
//    @Override
//    public boolean deleteOwnerById(UUID id) {
//        ownerDTOMap.remove(id);
//        return true;
//    }
//
//    @Override
//    public Optional<OwnerDTO> patchOwnerById(UUID id, OwnerDTO ownerDTO) {
//        com.PetClinic.model.OwnerDTO existing = ownerDTOMap.get(id);
//
//        if (StringUtils.hasText(ownerDTO.getName())) {
//            existing.setName(ownerDTO.getName());
//        }
//        if (StringUtils.hasText(ownerDTO.getAddress())) {
//            existing.setAddress(ownerDTO.getAddress());
//        }
//        if (StringUtils.hasText(ownerDTO.getTelephone())) {
//            existing.setTelephone(ownerDTO.getTelephone());
//        }
//        return Optional.of(existing);
//    }
//}
