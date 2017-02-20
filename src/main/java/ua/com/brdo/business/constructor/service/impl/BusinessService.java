package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.BusinessRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

@Service
public class BusinessService {

    private BusinessRepository businessRepo;

    @Autowired
    public BusinessService(BusinessRepository businessRepo) {
        this.businessRepo = businessRepo;
    }

    @Transactional
    public Business create(final Business business) {
        Objects.requireNonNull(business);
        return businessRepo.saveAndFlush(business);
    }

    @Transactional
    public Business update(final Business business) {
        Objects.requireNonNull(business);
        return businessRepo.saveAndFlush(business);
    }

    @Transactional
    public void delete(final long id) {
        businessRepo.delete(id);
    }

    @Transactional
    public void delete(final Business business) {
        businessRepo.delete(business);
    }

    public Business findById(final long id) {
        Business business = businessRepo.findOne(id);
        if (business == null) {
            throw new NotFoundException("Specified business was not found.");
        }
        return business;
    }

    public Business findByTitle(final String title) {
        return businessRepo.findByTitle(title)
                .orElseThrow(
                        () -> new NotFoundException("Business with given title was not found."));
    }

    public List<Business> findByUser(final User user) {
        return businessRepo.findByUser(user);
    }

    public List<Business> findAll() {
        return businessRepo.findAll();
    }

    public void checkBusinessOwnership( Business business, Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Access denied. You should be logged in to view business or answer questions.");
        }
        User user = (User) authentication.getPrincipal();
        User businessOwner = business.getUser();
        if (!businessOwner.equals(user)) {
            throw new AccessDeniedException("Access denied. You can't view this business or answer questions about it. It's not your business.");
        }
    }
}
