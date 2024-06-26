package com.project.demo.controllers;

import com.project.demo.Exceptions.AuthorisationException;
import com.project.demo.Utils.Authenticator;
import com.project.demo.Zoo.Animal;
import com.project.demo.Zoo.Enclosure;
import com.project.demo.Zoo.Privileges;
import com.project.demo.Zoo.Zookeeper;
import com.project.demo.ZooApplication;
import com.project.demo.components.ResultComponent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ListController extends DefaultController {
    public ChoiceBox<String> listModeInput;
    public VBox listResultsContainer;

    public void submitListChoiceInput(ActionEvent actionEvent) {
        String listMode = listModeInput.getSelectionModel().getSelectedItem().toLowerCase();
        listResultsContainer.getChildren().clear();

        switch (listMode) {
            case "zookeepers" -> listZookeepers();
            case "animals" -> listAnimals();
            case "enclosures" -> listEnclosures();
        }
    }

    private void listZookeepers() {
        try {
            Authenticator.authorise(Privileges.ZOOKEEPERS_AND_ABOVE);
        } catch (AuthorisationException error) {
            Label noPermissionLabel = new Label("You do not have permission to list zookeepers.");
            noPermissionLabel.getStyleClass().add("generic-centered-label");
            listResultsContainer.getChildren().add(noPermissionLabel);
            return;
        }

        ArrayList<Zookeeper> zookeepers = ZooApplication.zoo.zookeepers;
        if (zookeepers.isEmpty()) {
            Label noZookeepersLabel = new Label("There are no zookeepers in this zoo.");
            noZookeepersLabel.getStyleClass().add("generic-centered-label");
            listResultsContainer.getChildren().add(noZookeepersLabel);
            return;
        }

        if (Authenticator.getPrivilege() == Privileges.ZOOKEEPER) {
            if (Authenticator.getEmployee() == null) {
                System.err.println("Broken state. The privilege for the zookeeper is defined but not the employee.");
                Platform.exit();
                return;
            }

            listResultsContainer.getChildren().add(
                    prepareZookeeperResultComponent((Zookeeper) Authenticator.getEmployee())
            );

            Label onlyShowingThisZookeeperLabel = new Label("Only showing information about you.");
            onlyShowingThisZookeeperLabel.getStyleClass().add("generic-centered-label");
            listResultsContainer.getChildren().add(onlyShowingThisZookeeperLabel);

            return;
        }

        for (Zookeeper zookeeper : zookeepers) {
            listResultsContainer.getChildren().add(prepareZookeeperResultComponent(zookeeper));
        }
    }


    private void listEnclosures() {
        ArrayList<Enclosure> enclosures = ZooApplication.zoo.enclosures;
        if (enclosures.isEmpty()) {
            Label noEnclosuresLabel = new Label("There are no enclosures in this zoo.");
            noEnclosuresLabel.getStyleClass().add("generic-centered-label");
            listResultsContainer.getChildren().add(noEnclosuresLabel);
            return;
        }

        int enclosureIndex = 1;
        for (Enclosure enclosure : enclosures) {
            ResultComponent resultComponent = new ResultComponent();

            resultComponent.setText(
                    String.format("Enclosure %d • %s housed • %d animal%s • %d capacity • %s", enclosureIndex, enclosure.speciesHoused, enclosure.animals.size(), enclosure.animals.size() != 1 ? "s" : "", enclosure.capacity, enclosure.getId())
            );

            listResultsContainer.getChildren().add(resultComponent);
            enclosureIndex++;
        }
    }

    private void listAnimals() {
        ArrayList<Enclosure> enclosures = ZooApplication.zoo.enclosures;
        if (enclosures.isEmpty()) {
            Label noAnimalsLabel = new Label("There are no animals in this zoo.");
            noAnimalsLabel.getStyleClass().add("generic-centered-label");
            listResultsContainer.getChildren().add(noAnimalsLabel);
            return;
        }

        int animalsCount = ZooApplication.zoo.getAnimalsCount();
        Label animalInfoLabel = new Label(
                String.format(
                        "%d animal%s • %d species",
                        animalsCount,
                        animalsCount != 1 ? "s" : "",
                        ZooApplication.zoo.getSpeciesCount()
                ));


        animalInfoLabel.getStyleClass().add("generic-centered-label");
        listResultsContainer.getChildren().add(animalInfoLabel);

        int enclosureIndex = 1;
        for (Enclosure enclosure : enclosures) {
            ResultComponent enclosureResultComponent = new ResultComponent();
            enclosureResultComponent.setText(
                    String.format("Enclosure %d • %s housed • %d animal%s • %d capacity • %s", enclosureIndex, enclosure.speciesHoused, enclosure.animals.size(), enclosure.animals.size() != 1 ? "s" : "", enclosure.capacity, enclosure.getId())
            );

            listResultsContainer.getChildren().add(enclosureResultComponent);

            ArrayList<Animal> animals = enclosure.animals;
            if (animals.isEmpty()) {
                Label noAnimalsLabel = new Label("There are no animals in this enclosure.");
                noAnimalsLabel.getStyleClass().add("generic-centered-label");
                listResultsContainer.getChildren().add(noAnimalsLabel);
                enclosureIndex++;

                continue;
            }

            for (Animal animal : animals) {
                ResultComponent animalResultComponent = new ResultComponent();
                animalResultComponent.setText(
                        String.format("%s • %s • %s • %d year%s old • %s", animal.name, animal.species, animal.sex, animal.age, animal.age != 1 ? "s" : "", animal.healthy ? "Healthy" : "Unhealthy")
                );

                listResultsContainer.getChildren().add(animalResultComponent);
                animalResultComponent.getStyleClass().add("result-component--indented");
            }

            enclosureIndex++;
        }
    }

    private static ResultComponent prepareZookeeperResultComponent(Zookeeper zookeeper) {
        ResultComponent resultComponent = new ResultComponent();
        resultComponent.setText(
                String.format("%s • %s • %s • %s • Salary: %d • %d month%s worked", zookeeper.getId(), zookeeper.name, !zookeeper.getJob().isEmpty() ? zookeeper.getJob() : "No job set", zookeeper.sex, zookeeper.getSalary(), zookeeper.getWorkedMonths(), zookeeper.getWorkedMonths() != 1 ? "s" : "")
        );

        return resultComponent;
    }
}
