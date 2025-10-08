package cloudflight.integra.backend.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "cloudflight.integra.backend..", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureRulesTest {
    // Controller rules
    @ArchTest
    static final ArchRule controllers_should_reside_in_controller_package =
            classes().that().haveNameMatching(".*Controller").should().resideInAnyPackage("..controller..");

    @ArchTest
    static final ArchRule controllers_should_not_access_repo_directly = noClasses()
            .that()
            .resideInAnyPackage("..controller..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..repository..");

    // Service rules
    @ArchTest
    static final ArchRule services_should_reside_in_service_package =
            classes().that().haveNameMatching(".*Service").should().resideInAnyPackage("..service..");

    @ArchTest
    static final ArchRule service_should_not_depend_on_controllers = noClasses()
            .that()
            .resideInAnyPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..controller..");

    // Repo rules
    @ArchTest
    static final ArchRule repo_should_not_depend_on_service_or_controller = noClasses()
            .that()
            .resideInAnyPackage("..repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..service..", "..controller");

    @ArchTest
    static final ArchRule repo_should_reside_in_repo_package =
            classes().that().haveNameMatching(".*Repository").should().resideInAnyPackage("..repository..");

    // DTO rules
    @ArchTest
    static final ArchRule dto_should_reside_in_dto_package =
            classes().that().haveNameMatching(".*DTO").should().resideInAnyPackage("..dto..");

    // Mapper rules
    @ArchTest
    static final ArchRule mapper_should_reside_in_mapper_package =
            classes().that().haveNameMatching(".*Mapper").should().resideInAnyPackage("..mapper..");
}
