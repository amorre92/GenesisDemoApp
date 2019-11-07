package amorre.genesis.demo.controller;

import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.service.ContactLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Anthony Morre
 */
@RestController
public class ContactController {

    private final ContactLifecycleService contactLifecycleService;
    private final ContactRepository contactRepository;

    @Autowired
    public ContactController(ContactLifecycleService contactLifecycleService,
                             ContactRepository contactRepository) {
        this.contactLifecycleService = contactLifecycleService;
        this.contactRepository = contactRepository;
    }

    @GetMapping("/contacts")
    @Transactional(readOnly = true)
    public List<ContactDto> getAllContacts() {
        return contactRepository.findAll()
                .stream()
                .map(ContactDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/contacts/{id}")
    @Transactional(readOnly = true)
    public ContactDto getContact(@PathVariable String id) {
        return contactRepository.findById(id)
                .map(ContactDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find contact with id {0}", id));
    }

    @PostMapping("/contacts")
    @Transactional
    public ContactDto createContact(@RequestBody ContactDto contactDto) {

        return ContactDto.from(contactLifecycleService.createContact(contactDto));
    }

    @PutMapping("/contacts/{id}")
    @Transactional
    public ContactDto updateContact(@PathVariable("id") String id, @RequestBody ContactDto contactDto) {
        Assert.isTrue(contactDto.getId().equals(id), "payload mismatch between received contactDto and desired id");

        return ContactDto.from(contactLifecycleService.updateContact(contactDto));
    }

    @DeleteMapping("/contacts/{id}")
    @Transactional
    public void deleteContact(@PathVariable("id") String id) {
        contactLifecycleService.deleteContact(id);
    }
}
