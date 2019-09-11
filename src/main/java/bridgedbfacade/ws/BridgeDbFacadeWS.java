package bridgedbfacade.ws;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.bridgedb.IDMapperException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import bridgedbfacade.BridgeDbFacade;
import bridgedbfacade.model.XrefSet;
import io.swagger.annotations.ApiOperation;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
public class BridgeDbFacadeWS {

	@Value("${bridgedb.config:/data/gdb.config}")
	private String bridgedbConfig;
	
	BridgeDbFacade bdbfacade;
	
    public BridgeDbFacadeWS() {
    	try {
			//bdbfacade = new BridgeDbFacade(this.bridgedbConfig);
			bdbfacade = new BridgeDbFacade("/data/gdb.config");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IDMapperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//System.out.println("hello");
    }
    
    @ApiOperation(value = "Map Xref")
    @GetMapping("/xrefs/{identifier}")
    public  Set<XrefSet> xrefs(@RequestParam(value="identifier", defaultValue="1053_at") String identifier) {
        Set<XrefSet> retVal = null;
    	try {
			retVal = this.bdbfacade.xrefs(identifier);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IDMapperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
    }

    @ApiOperation(value = "Batch Map Xref")
    @PostMapping("/xrefsBatch/")
    public  Set<Set<XrefSet>> xrefsBatch(@RequestBody String idListString) {
        Set<XrefSet> specieResult = null;
        Set<Set<XrefSet>> setSpecieResult = new HashSet<Set<XrefSet>>();
        
        String[] ids = idListString.split("\n");
        
        for(String id : ids) {
        	try {
        		specieResult = this.bdbfacade.xrefs(id);

       			setSpecieResult.add(specieResult);
			} catch (ClassNotFoundException | IDMapperException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
		return setSpecieResult;
    }
    

}