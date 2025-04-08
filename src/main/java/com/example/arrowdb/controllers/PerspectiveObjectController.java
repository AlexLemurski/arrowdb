package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.FileNode;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.PerspectiveObject;
import com.example.arrowdb.enums.PerspectiveObjectENUM;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.PerspectiveObjectService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.arrowdb.auxiliary.FilePathResource.BASE_DIRECTORY_PERSPECTIVE;
import static com.example.arrowdb.auxiliary.Message.UNIQUE_DEPARTMENT;
import static com.example.arrowdb.auxiliary.Message.UNIQUE_PERSPECTIVE;

@Controller
@RequiredArgsConstructor
public class PerspectiveObjectController {

    private final PerspectiveObjectService perspectiveObjectService;
    private final EmployeeService employeeService;

    @GetMapping("/general/perspective/perspectiveDocument/path/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_VIEW')")
    public String listFilesPathById(@PathVariable("id") int id, Model model) {
        PerspectiveObject perspectiveObject = perspectiveObjectService.findPerspectiveObjectById(id);
        String directoryPath = (perspectiveObject.getFilePath() == null ||
                perspectiveObject.getFilePath().isEmpty()) ? BASE_DIRECTORY_PERSPECTIVE : perspectiveObject.getFilePath();
        File directory = new File(directoryPath);
        List<FileNode> files = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    files.add(new FileNode(file.getName(), file.getAbsolutePath(), file.isDirectory()));
                }
            }
        }
        model.addAttribute("perspectiveObject", perspectiveObject);
        model.addAttribute("files", files);
        model.addAttribute("currentPath", directoryPath);
        return "perspective/perspective-folder";
    }

    @GetMapping("/general/perspective/perspectiveDocument/path")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_VIEW')")
    public String listFilesPath(@RequestParam(required = false) String path, Model model) {
        String directoryPath = (path == null || path.isEmpty()) ? BASE_DIRECTORY_PERSPECTIVE : path;
        File directory = new File(directoryPath);
        List<FileNode> files = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    files.add(new FileNode(file.getName(), file.getAbsolutePath(), file.isDirectory()));
                }
            }
        }
        model.addAttribute("files", files);
        model.addAttribute("currentPath", directoryPath);
        return "perspective/perspective-folder";
    }

    @GetMapping("/general/perspective/perspectiveDocument/open")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_VIEW')")
    public void downloadFile(@RequestParam("filePath") String filePath,
                             HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            String fileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
            response.setContentType("application/" + file.getName()
                    .substring(file.getName().lastIndexOf(".") + 1));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            Files.copy(file.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @GetMapping("/general/perspective")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_VIEW')")
    public String getPerspectiveList(@NotNull Model model) {
        List<PerspectiveObject> perspectiveList = perspectiveObjectService.findAllPerspectiveObjects();
        model.addAttribute("perspectiveList", perspectiveList);
        return "perspective/perspective-menu";
    }

    @GetMapping("/general/perspective/perspectiveView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_VIEW')")
    public String findPerspectiveById(@PathVariable("id") int id, Model model) {
        model.addAttribute("perspectiveObject", perspectiveObjectService.findPerspectiveObjectById(id));
        return "perspective/perspective-view";
    }

    @GetMapping("/general/perspective/perspectiveCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_CREATE')")
    public String createPerspectiveForm(@ModelAttribute PerspectiveObject perspectiveObject,
                                       Model model) {
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        return "perspective/perspective-create";
    }

    @PostMapping("/general/perspective/perspectiveCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_CREATE')")
    public String createPerspective(@Valid @ModelAttribute PerspectiveObject perspectiveObject,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
            return "perspective/perspective-create";
        } else {
            try {
                perspectiveObject.setPerspectiveObjectENUM(PerspectiveObjectENUM.IN_PROGRESS);
                perspectiveObjectService.savePerspectiveObject(perspectiveObject);
                return "redirect:/general/perspective";
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_DEPARTMENT);
                return "perspective/perspective-create";
            }
        }
    }

    @GetMapping("/general/perspective/perspectiveDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DELETE')")
    public String deletePerspective(@PathVariable("id") int id) {
        perspectiveObjectService.deletePerspectiveObjectById(id);
        return "redirect:/general/perspective";
    }

    @GetMapping("/general/perspective/perspectiveUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_UPDATE')")
    public String updatePerspectiveForm(@PathVariable("id") int id,
                                       Model model) {
        PerspectiveObject perspectiveObject = perspectiveObjectService.findPerspectiveObjectById(id);
        if (perspectiveObject.getPerspectiveObjectENUM().equals(PerspectiveObjectENUM.ENDED)) {
            return "redirect:/general/perspective/perspectiveView/%d"
                    .formatted(perspectiveObject.getPerspectiveId());
        }
        model.addAttribute("perspectiveStatus", PerspectiveObjectENUM.values());
        model.addAttribute("perspectiveObject", perspectiveObject);
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        return "perspective/perspective-update";
    }

    @PostMapping("/general/perspective/perspectiveUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_UPDATE')")
    public String updatePerspective(@Valid @ModelAttribute PerspectiveObject perspectiveObject,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentStatus", ProfAndDepStatusENUM.values());
            model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
            return "perspective/perspective-update";
        } else {
            try {
                if(perspectiveObject.getPerspectiveObjectENUM().equals(PerspectiveObjectENUM.ENDED)){
                    perspectiveObject.setDateOfEnd(LocalDateTime.now());
                }
                perspectiveObjectService.savePerspectiveObject(perspectiveObject);
                return "redirect:/general/perspective/perspectiveView/%d"
                        .formatted(perspectiveObject.getPerspectiveId());
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_PERSPECTIVE);
                return "perspective/perspective-update";
            }
        }
    }
}