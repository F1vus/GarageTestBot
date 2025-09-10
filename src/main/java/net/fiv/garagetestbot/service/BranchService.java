package net.fiv.garagetestbot.service;

import lombok.RequiredArgsConstructor;
import net.fiv.garagetestbot.model.Branch;
import net.fiv.garagetestbot.repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Branch findBranchById(Long id) {
        return branchRepository.findById(id).orElse(null);
    }
}
