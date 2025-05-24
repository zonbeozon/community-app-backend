package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.service.ChannelEntityQueryService;
import com.zonbeozon.channel.service.ChannelMemberEntityQueryService;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.reaction.entity.Reaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.exception.ReactionAlreadyExistException;
import com.zonbeozon.reaction.exception.ReactionNotFoundException;
import com.zonbeozon.reaction.service.dto.ReactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
abstract class AbstractReactionService<T, R extends Reaction> {
    private final ChannelMemberEntityQueryService channelMemberEntityQueryService;
    private final ChannelEntityQueryService channelEntityQueryService;

    @Transactional
    public void markAsLike(Long channelId, Long articleId, Member member) {
        mark(channelId, articleId, ReactionType.LIKE, member);
    }

    @Transactional
    public void markAsDislike(Long channelId, Long articleId, Member member) {
        mark(channelId, articleId, ReactionType.DISLIKE, member);
    }

    private void mark(Long channelId, Long articleId, ReactionType reactionType, Member member) {
        consumeIfChannelMemberAndArticleExist(channelId, member, articleId, (channelMember, article) -> {
            //중복이지만 다른 리엑션 타입이라면 변경 or 같은 리엑션 타입이면 예외 발생
            findReactionByArticleAndAuthor(article, channelMember)
                    .ifPresentOrElse(
                            existingReaction -> {
                                // 동일한 리액션 타입이면 예외 발생
                                if (existingReaction.getReactionType() == reactionType) {
                                    throw new ReactionAlreadyExistException("이미 리엑션 체크를 했습니다.");
                                }
                                // 다른 리액션 타입이면 변경
                                existingReaction.updateReactionType(reactionType);
                                save(existingReaction);
                            },
                            () -> {
                                // 새로운 리액션 추가
                                R reaction = createReaction(article, reactionType, channelMember);
                                save(reaction);
                            }
                    );
        });
    }

    @Transactional
    public void unmark(Long channelId, Long articleId, Member member) {
        consumeIfChannelMemberAndArticleExist(channelId, member, articleId, (channelMember, article) -> {
            R reaction = findReactionByArticleAndAuthor(article, channelMember)
                    .orElseThrow(() -> new ReactionNotFoundException("해당 유저는 이전에 아무런 리엑션을 남기지 않았습니다."));
            delete(reaction);
        });
    }

    @Transactional(readOnly = true)
    public ReactionResponse createReactionResponse(Long articleId) {
        T article = getArticleByIdOrThrow(articleId);
        //stream의 collect를 이용해서 like, dislike 개수를 카운팅
        List<R> reactions = findReactionByArticle(article);
        Map<ReactionType, Long> reactionTypeCount = calReactionCountByReactionType(reactions);
        return new ReactionResponse(reactionTypeCount);
    }

    @Transactional(readOnly = true)
    public ReactionResponse createReactionResponse(Long articleId, Long channelId, Member member) {
        return applyIfChannelMemberAndArticleExist(channelId, member, articleId, (channelMember, article) -> {
            ReactionResponse reactionResponse = createReactionResponse(articleId);
            //유저가 로그인 상태이기 때문에 유저의 현제 리엑션 상태를 반영
            updateReactionByCurrentMember(article, reactionResponse, channelMember);
            return reactionResponse;
        });
    }

    private Map<ReactionType, Long> calReactionCountByReactionType(List<R> reactions) {
        return reactions.stream().collect(Collectors.groupingBy(Reaction::getReactionType, Collectors.counting()));
    }

    private void updateReactionByCurrentMember(
            T article,
            ReactionResponse reactionResponse,
            ChannelMember channelMember
    ) {
        Optional<R> optReaction = findReactionByArticleAndAuthor(article, channelMember);
        if(optReaction.isEmpty()) return; //사용자가 아무런 리엑션을 하지 않았다면
        //DTO에 현재 사용자의 리액션 상태 반영
        reactionResponse.setReactionByCurrentMember(optReaction.get().getReactionType());
    }

    private <RT> RT applyIfChannelMemberAndArticleExist(Long channelId, Member member, Long articleId, BiFunction<ChannelMember, T, RT> function) {
        Channel channel = channelEntityQueryService.getChannelByIdOrThrow(channelId);
        ChannelMember channelMember = channelMemberEntityQueryService.getByMemberAndChannelOrThrow(member, channel);
        T article = getArticleByIdOrThrow(articleId);
        return function.apply(channelMember, article);
    }

    private void consumeIfChannelMemberAndArticleExist(Long channelId, Member member, Long articleId, BiConsumer<ChannelMember, T> consumer) {
        applyIfChannelMemberAndArticleExist(channelId, member, articleId, (channelMember, article) -> {
            consumer.accept(channelMember, article);
            return null;
        });
    }

    protected abstract T getArticleByIdOrThrow(Long articleId);
    protected abstract Optional<R> findReactionByArticleAndAuthor(T article, ChannelMember channelMember);
    protected abstract List<R> findReactionByArticle(T article);
    protected abstract void save(R reaction);
    protected abstract void delete(R reaction);
    protected abstract R createReaction(T article, ReactionType reactionType, ChannelMember channelMember);
}
