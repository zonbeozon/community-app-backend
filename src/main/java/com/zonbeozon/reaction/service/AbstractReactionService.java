package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.reaction.entity.Reaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.exception.ReactionNotFoundException;
import com.zonbeozon.reaction.service.dto.ReactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
abstract class AbstractReactionService<T, R extends Reaction> {

    @Transactional
    public void markAsLike(ChannelMember channelMember, Long articleId) {
        mark(channelMember, articleId, ReactionType.LIKE);
    }

    @Transactional
    public void markAsDislike(ChannelMember channelMember, Long articleId) {
        mark(channelMember, articleId, ReactionType.DISLIKE);
    }

    private void mark(ChannelMember channelMember, Long articleId, ReactionType reactionType) {
        T article = getArticleByIdOrThrow(articleId);
        //중복이지만 다른 리엑션 타입이라면 변경 or 같은 리엑션 타입이면 예외 발생
        Optional<R> optReaction = getReactionByArticleAndAuthor(article, channelMember);
        optReaction.ifPresentOrElse(
                existingReaction -> {
                    existingReaction.validateUpdateReactionType(reactionType);
                    existingReaction.updateReactionType(reactionType);
                    },
                () -> {
                    // 새로운 리액션 추가
                    R reaction = createReaction(article, reactionType, channelMember);
                    save(reaction);
                });
    }

    @Transactional
    public void unmark(ChannelMember channelMember, Long articleId) {
        T article = getArticleByIdOrThrow(articleId);
        R reaction = getReactionByArticleAndAuthor(article, channelMember)
                .orElseThrow(() -> new ReactionNotFoundException("해당 유저는 이전에 아무런 리엑션을 남기지 않았습니다."));
        delete(reaction);
    }


    @Transactional(readOnly = true)
    public ReactionResponse createReactionResponse(ChannelMember channelMember, Long articleId) {
        T article = getArticleByIdOrThrow(articleId);
        //stream의 collect를 이용해서 like, dislike 개수를 카운팅
        List<R> reactions = getReactionByArticle(article);
        Map<ReactionType, Long> reactionTypeCount = calReactionCountByReactionType(reactions);
        ReactionResponse reactionResponse = new ReactionResponse(reactionTypeCount);
        if(channelMember != null) {
            //유저가 로그인 상태이기 때문에 유저의 현제 리엑션 상태를 반영
            updateReactionByCurrentMember(article, reactionResponse, channelMember);
        }
        return reactionResponse;
    }

    private Map<ReactionType, Long> calReactionCountByReactionType(List<R> reactions) {
        return reactions.stream().collect(Collectors.groupingBy(Reaction::getReactionType, Collectors.counting()));
    }

    private void updateReactionByCurrentMember(
            T article,
            ReactionResponse reactionResponse,
            ChannelMember channelMember
    ) {
        Optional<R> optReaction = getReactionByArticleAndAuthor(article, channelMember);
        if(optReaction.isEmpty()) return; //사용자가 아무런 리엑션을 하지 않았다면
        //DTO에 현재 사용자의 리액션 상태 반영
        reactionResponse.setReactionByCurrentMember(optReaction.get().getReactionType());
    }

    protected abstract T getArticleByIdOrThrow(Long articleId);
    protected abstract Optional<R> getReactionByArticleAndAuthor(T article, ChannelMember channelMember);
    protected abstract List<R> getReactionByArticle(T article);
    protected abstract void save(R reaction);
    protected abstract void delete(R reaction);
    protected abstract R createReaction(T article, ReactionType reactionType, ChannelMember channelMember);
}
