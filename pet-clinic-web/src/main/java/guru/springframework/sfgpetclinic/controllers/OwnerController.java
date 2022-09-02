package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    public static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM="owners/createOrUpdateOwnerForm";

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }


    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping("/find")
    public String findOwners(Model model){
        model.addAttribute("owner", Owner.builder().build());
        return "owners/findOwners";
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwner(@PathVariable String ownerId){
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(ownerService.findByID(Long.parseLong(ownerId)));
        return mav;
    }

    @GetMapping("")
    public String processFindForm(Owner owner, BindingResult result, Model model){
        if(owner.getLastName()==null){
            owner.setLastName("");
        }

        List<Owner> results = this.ownerService.findAllByLastNameLike("%"+owner.getLastName()+"%");

        if(results.isEmpty()){
            result.rejectValue("lastName" ,"notFound", "not found");
            return "owners/findOwners";
        } else if(results.size()==1){
            owner=results.iterator().next();
            return "redirect:/owners/"+owner.getId();
        } else {
            model.addAttribute("selections", results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/new")
    public String initCreationForm(Model model){
        model.addAttribute("owner", new Owner());
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String processCreationForm(Owner owner, BindingResult result){
        if(result.hasErrors()){
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }else {
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/"+savedOwner.getId();
        }
    }


    @GetMapping("/{ownerId}/edit")
    public String initEditForm(Model model, @PathVariable String ownerId){
        model.addAttribute("owner", ownerService.findByID(Long.parseLong(ownerId)));
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/{ownerId}/edit")
    public String processEditForm(Owner owner, BindingResult result, @PathVariable String ownerId){
        if(result.hasErrors()){
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }else {
//            Owner dbOwner = ownerService.findByID(Long.parseLong(ownerId));
//            dbOwner.setFirstName(owner.getFirstName());
//            dbOwner.setLastName(owner.getLastName());
//            dbOwner.setCity(owner.getCity());
//            dbOwner.setTelephone(owner.getTelephone());
//            dbOwner.setAddress(owner.getAddress());
//            dbOwner.setPets(owner.getPets());
            owner.setId(Long.parseLong(ownerId));
            Owner savedOwner = ownerService.save(owner);
            return "redirect:/owners/"+savedOwner.getId();
        }
    }

}
