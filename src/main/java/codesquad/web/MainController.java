package codesquad.web;

import codesquad.web.domain.QuestionRepository;
import codesquad.web.util.RepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public String list(Model model){
        model.addAttribute("questions", RepositoryUtil.findQuestions(questionRepository));
        return "/index";
    }
}
