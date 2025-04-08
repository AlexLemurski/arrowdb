package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.FileNode;
import com.example.arrowdb.entity.WorkObject;
import com.example.arrowdb.entity_service.WorkObjectServiceEntity;
import com.example.arrowdb.enums.WorkObjectStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.WorkObjectService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.arrowdb.auxiliary.FilePathResource.BASE_DIRECTORY_WORK_OBJECT;
import static com.example.arrowdb.auxiliary.Message.DELETE_OR_CHANGE_STATUS_WORK_OBJECT_MESSAGE;
import static com.example.arrowdb.auxiliary.Message.UNIQUE_WORK_OBJECT;

@Controller
@RequiredArgsConstructor
public class WorkObjectController {

    private final WorkObjectService workObjectService;
    private final EmployeeService employeeService;
    private final WorkObjectServiceEntity workObjectServiceEntity;

    @GetMapping("/general/workObject/workObjectDocument/path/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_VIEW')")
    public String listFilesPathById(@PathVariable("id") int id, Model model) {
        WorkObject workObject = workObjectService.findWorkObjectById(id);
        String directoryPath = (workObject.getFilePath() == null ||
                workObject.getFilePath().isEmpty()) ? BASE_DIRECTORY_WORK_OBJECT : workObject.getFilePath();
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
        model.addAttribute("workObject", workObject);
        model.addAttribute("files", files);
        model.addAttribute("currentPath", directoryPath);
        return "perspective/perspective-folder";
    }

    @GetMapping("/general/workObject/workObjectDocument/path")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_VIEW')")
    public String listFilesPath(@RequestParam(required = false) String path, Model model) {
        String directoryPath = (path == null || path.isEmpty()) ? BASE_DIRECTORY_WORK_OBJECT : path;
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

    @GetMapping("/general/workObject/workObjectDocument/open")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_VIEW')")
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

    @GetMapping("/general/workobject")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_VIEW')")
    public String getWorkObjectList(Model model) {
        List<WorkObject> workObjects = workObjectService.findAllWorkObjectForMainMenu();
        model.addAttribute("workObjects", workObjects);
        model.addAttribute("workObjectStatus", WorkObjectStatusENUM.values());
        return "work_object/work_object-menu";
    }

    @GetMapping("/general/workobject/workobjectView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_VIEW')")
    public String findWorkObjectById(@PathVariable("id") int id,
                                     Model model) {
        model.addAttribute("workObject", workObjectService.findWorkObjectByIdForView(id));
        return "work_object/work_object-view";
    }

    @GetMapping("/general/workobject/workobjectCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_CREATE')")
    public String createWorkObjectForm(@ModelAttribute WorkObject workObject) {
        workObject.setWorkObjectStatusENUM(WorkObjectStatusENUM.NOT_STARTED);
        return "work_object/work_object-create";
    }

    @PostMapping("/general/workobject/workobjectCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_CREATE')")
    public String createWorkObject(@Valid WorkObject workObject,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "work_object/work_object-create";
        } else {
            try {
                workObject.setWorkObjectStatusENUM(WorkObjectStatusENUM.NOT_STARTED);
                workObjectService.saveWorkObject(workObject);
                return "redirect:/general/workobject";
            } catch (Exception e) {
                model.addAttribute("errorName",UNIQUE_WORK_OBJECT);
                return "work_object/work_object-create";
            }
        }
    }

    @GetMapping("/general/workobject/workobjectDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DELETE')")
    public String deleteWorkObject(@PathVariable("id") int id,
                                   Model model) {
        try {
            workObjectService.deleteWorkObjectById(id);
        } catch (Exception e) {
            WorkObject workObject = workObjectService.findWorkObjectById(id);
            model.addAttribute("workObject", workObject);
            model.addAttribute("error", DELETE_OR_CHANGE_STATUS_WORK_OBJECT_MESSAGE);
            return "error/work_object-error";
        }
        return "redirect:/general/workobject";
    }

    @GetMapping("/general/workobject/workobjectUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_UPDATE')")
    public String updateWorkObjectForm(@PathVariable("id") int id,
                                       Model model) {
        WorkObject workObject = workObjectService.findWorkObjectById(id);
        if(workObject.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.CLOSED)){
            return "redirect:/general/workobject/workobjectView/%d".formatted(workObject.getWorkObjectId());
        }
        model.addAttribute("workObject", workObject);
        model.addAttribute("workObjectStatusList", Arrays.stream(WorkObjectStatusENUM.values())
                .filter(e -> !e.equals(WorkObjectStatusENUM.NOT_STARTED))
                .toList());
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        return "work_object/work_object-update";
    }

    @PostMapping("/general/workobject/workobjectUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_UPDATE')")
    public String updateWorkObject(@Valid WorkObject workObject,
                                   @NotNull BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workObject", workObject);
            model.addAttribute("workObjectStatusList", Arrays.stream(WorkObjectStatusENUM.values())
                    .filter(e -> !e.equals(WorkObjectStatusENUM.NOT_STARTED))
                    .toList());
            model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
            return "work_object/work_object-update";
        } else {
            WorkObject workObjectById = workObjectService.findWorkObjectById(workObject.getWorkObjectId());
            if (workObjectServiceEntity.checkWorkObjectRelationshipsBeforeClose(workObjectById, workObject)) {
                model.addAttribute("workObject", workObjectById);
                model.addAttribute("constructionControlListActive", workObjectById
                        .getConstructionControlList()
                        .stream()
                        .filter(e -> e.equals(WorkObjectStatusENUM.ACTIVE))
                        .toList());
                model.addAttribute("error", DELETE_OR_CHANGE_STATUS_WORK_OBJECT_MESSAGE);
                return "error/work_object-error";
            } else {
                workObjectServiceEntity.addCollectionEmployeeToEnvers(workObject);
                workObjectService.saveWorkObject(workObject);
            }
        }
        return "redirect:/general/workobject/workobjectView/%d".formatted(workObject.getWorkObjectId());
    }
}