package codesquad.web;

import codesquad.web.domain.User;
import codesquad.web.domain.UserRepository;
import codesquad.web.util.Util;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session) {
        Optional<User> maybeUser = userRepository.findByUserId(userId);
        User user = maybeUser.orElseThrow(() -> new IllegalArgumentException("해당 아이디를 찾을 수 없습니다."));
        if (user.matchPassword(password))
            session.setAttribute("sessionUser", user);
        return "redirect:/";
    }

    @PostMapping
    public String create(User user, Model model) {
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("user", Util.findUserById(id, userRepository));
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable long id, Model model, HttpSession session) {
        User user = Util.findUserById(id, userRepository);
        if (!user.equals((User) session.getAttribute("sessionUser"))) {
            model.addAttribute("errorMessage", "다른 사람의 정보는 수정할 수 없어..");
            return "common/errorPage";
        }
        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable long id, User newUser, HttpSession session) {
        //내 정보만 수정할 수 있도록 변경한다.
        User loginedUser = (User) session.getAttribute("sessionUser");
        User user = Util.findUserById(id, userRepository);
        if (!user.equals(loginedUser)) {
            throw new ForbiddenTargetException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        if (user.matchPassword(newUser)) {
            user.update(newUser);
            userRepository.save(user);
        }
        return "redirect:/users";
    }
}
