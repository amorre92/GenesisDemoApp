package amorre.genesis.demo.controller;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.repository.EnterpriseRepository;
import amorre.genesis.demo.service.EnterpriseLifecycleService;
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
public class EnterpriseController {

    private final EnterpriseLifecycleService enterpriseLifecycleService;
    private final EnterpriseRepository enterpriseRepository;

    @Autowired
    public EnterpriseController(EnterpriseLifecycleService enterpriseLifecycleService, EnterpriseRepository enterpriseRepository) {
        this.enterpriseLifecycleService = enterpriseLifecycleService;
        this.enterpriseRepository = enterpriseRepository;
    }

    @GetMapping("/enterprises")
    @Transactional
    public List<EnterpriseDto> getAllEnterprises() {
        return enterpriseRepository.findAll()
                .stream()
                .map(EnterpriseDto::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/enterprises/{id}")
    @Transactional
    public EnterpriseDto getEnterprise(@PathVariable("id") String id) {
        return EnterpriseDto.from(enterpriseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("cannot find enterprise with id {0}", id)));
    }

    @PostMapping("/enterprises")
    @Transactional
    public EnterpriseDto createEnterprise(@RequestBody EnterpriseDto enterpriseDto) {
        return EnterpriseDto.from(enterpriseLifecycleService.createEnterprise(enterpriseDto));
    }

    @PutMapping("/enterprises/{id}")
    @Transactional
    public EnterpriseDto updateEnterprise(@PathVariable("id") String id, @RequestBody EnterpriseDto enterpriseDto) {
        Assert.isTrue(enterpriseDto.getId().equals(id), "payload mismatch between received enterpriseDto and desired id");

        return EnterpriseDto.from(enterpriseLifecycleService.updateEnterprise(enterpriseDto));
    }

    @PutMapping("/enterprises/{id}/address")
    public EnterpriseDto updateEnterpriseAddAddress(@PathVariable("id") String id, @RequestBody AddressDto addressDto,
                                                    @RequestParam(value = "replaceHeadOffice", required = false, defaultValue = "false") Boolean replaceHeadOffice) {
        return EnterpriseDto.from(enterpriseLifecycleService.addAddressToEnterprise(id, addressDto, replaceHeadOffice));
    }

    @DeleteMapping("/enterprises/{id}")
    @Transactional
    public void deleteEnterprise(@PathVariable("id") String id) {
        enterpriseLifecycleService.deleteEnterprise(id);
    }

}
