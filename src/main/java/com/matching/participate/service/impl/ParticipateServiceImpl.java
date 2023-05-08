package com.matching.participate.service.impl;

import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.participate.domain.Participate;
import com.matching.participate.dto.ParticipateStatusRequest;
import com.matching.participate.repository.ParticipateRepository;
import com.matching.participate.service.ParticipateService;
import com.matching.post.domain.Post;
import com.matching.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService {
    private final ParticipateRepository participateRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    @Override
    public Long insertParticipate(Long id, Long postId) {
        Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        return participateRepository.save(Participate.from(member, post)).getId();
    }

    @Override
    public Long updateParticipateStatus(Long id, Long postId, ParticipateStatusRequest parameter) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        Member member = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        if(!post.getAuthor().getId().equals(id)) {
            throw new IllegalArgumentException("참가자 상태 변경 권한이 없습니다.");
        }

        Participate participate = participateRepository.findByParticipate_IdAndPost_Id(member.getId(), post.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 참가자를 조회할 수 없습니다."));

        participate.updateStatus(Participate.ParticipateStatus.ADMISSION);

        participateRepository.save(participate);

        return null;
    }
}