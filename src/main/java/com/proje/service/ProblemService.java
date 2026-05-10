package com.proje.service;

import com.proje.dto.ProblemForm;
import com.proje.entity.Problem;
import com.proje.entity.ProblemStatus;
import com.proje.entity.User;
import com.proje.repository.ProblemRepository;
import com.proje.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProblemService {

    public static final int APPROVAL_SCORE = 10;

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public ProblemService(ProblemRepository problemRepository, UserRepository userRepository) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Problem findById(Long id) {
        return problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bildirim bulunamadı: " + id));
    }

    @Transactional(readOnly = true)
    public List<Problem> findForUser(User user, String keyword) {
        if (StringUtils.hasText(keyword)) {
            return problemRepository.searchByOwner(user, keyword.trim());
        }
        return problemRepository.findByOwnerOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public List<Problem> findApprovedFeed() {
        return problemRepository.findByStatusOrderByCreatedAtDesc(ProblemStatus.APPROVED);
    }

    @Transactional(readOnly = true)
    public List<Problem> findAll(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return problemRepository.searchAll(keyword.trim());
        }
        return problemRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Problem> findAll(String keyword, ProblemStatus status) {
        String safeKeyword = StringUtils.hasText(keyword) ? keyword.trim() : "";
        return problemRepository.searchAllWithFilters(safeKeyword, status);
    }

    public Problem create(ProblemForm form, User owner) throws IOException {
        Problem problem = new Problem();
        applyForm(problem, form);
        problem.setOwner(owner);
        problem.setStatus(ProblemStatus.PENDING);
        return problemRepository.save(problem);
    }

    public Problem update(Long id, ProblemForm form) throws IOException {
        Problem problem = findById(id);
        applyForm(problem, form);
        return problemRepository.save(problem);
    }

    public void delete(Long id) {
        problemRepository.delete(findById(id));
    }

    public Problem changeStatus(Long id, ProblemStatus newStatus) {
        Problem problem = findById(id);
        ProblemStatus previousStatus = problem.getStatus();
        problem.setStatus(newStatus);

        if (!problem.isScoreAwarded()
                && !ProblemStatus.APPROVED.equals(previousStatus)
                && ProblemStatus.APPROVED.equals(newStatus)) {
            User owner = problem.getOwner();
            owner.addScore(APPROVAL_SCORE);
            problem.setScoreAwarded(true);
            userRepository.save(owner);
        }

        return problemRepository.save(problem);
    }

    public boolean canManage(User currentUser, Problem problem) {
        return currentUser != null &&
                (currentUser.isAdmin() || currentUser.equals(problem.getOwner()));
    }

    public ProblemForm toForm(Problem problem) {
        ProblemForm form = new ProblemForm();
        form.setTitle(problem.getTitle());
        form.setDescription(problem.getDescription());
        form.setCategory(problem.getCategory());
        return form;
    }

    private void applyForm(Problem problem, ProblemForm form) throws IOException {
        problem.setTitle(form.getTitle().trim());
        problem.setDescription(form.getDescription().trim());
        problem.setCategory(form.getCategory().trim());

        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            problem.setImageData(image.getBytes());
            problem.setImageContentType(image.getContentType());
        }
    }
}
