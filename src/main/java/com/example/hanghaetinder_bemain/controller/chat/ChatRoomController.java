package com.example.hanghaetinder_bemain.controller.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.hanghaetinder_bemain.dto.resoponse.chat.ChatRoomListDto;
import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.entity.Member;
import com.example.hanghaetinder_bemain.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.sevice.chat.ChatRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final MatchMemberRepository matchMemberRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomService chatRoomService;

    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @Transactional
    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity<ChatRoomListDto> room(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
        Optional<Member> member = memberRepository.findById(userDetails.getMember().getId());
        List<ChatRoom> matchMemberOptional = matchMemberRepository.findMatchmember(member.get().getId());
        if (matchMemberOptional.size() != 0) {

            ChatRoomListDto chatRoomListDto = ChatRoomListDto.from(matchMemberOptional);
            return ResponseEntity.ok().body(chatRoomListDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

}

