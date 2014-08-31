##What is Dragome?

Dragome is a software development kit (SDK) for creating RICH Internet Application(RIA). It provides developers a mechanism to write client side application in Java. 
Javascript code is generated directly from bytecode which gives a lot of advantages in comparison to source to source transformations.
Dragome is open source, completely free, and it is licensed under the GPL v3.0.

## Why to use Dragome?
* Code everything in Java (server side and client side), it will be transformed to javascript automatically.
* Higher level of abstraction for GUI development using components.
* High performance for generated client side code.
* Java based development process and code reuse.
* Use the excellent debugging support offered by the mature Java IDEs like Eclipse, developers can debug the client side application just as an Java Application.
* Make use of built-in IDE support to refactor Java code, improve your agile development.
* You will minimize contact with HTML files. Managed by graphics designers not developers, Dragome uses exactly the same files that designers are editing, just plain HTML files.
* UI design updates can be deployed by non developer roles because code is totally decoupled from HTML files.
* Use continuation in your development: you can pause your program and continue it whenever you need.
* Unit testing integration: you can also run your Junit tests on a browser.

## The Dragome modules
Dragome SDK can be divided into following five major parts:

* Dragome Bytecode to JavaScript compiler : This is the most important part of Dragome which makes it a powerful tool for building RIAs. Dragome compiler is used to translate all the application code written in Java into JavaScript.
* JRE Emulation library : Dragome includes a library that emulates a subset of the Java runtime library. 
* Form Bindings: Based on gwt-pectin project this module provides easy data bindings capabilities. It uses a declarative style API (guice style) for defining the models, commands and forms as well as binding them to components.
* Callback evictor: In charge of getting rid of callback hell, it uses bytecode instrumentation and dynamic proxies to do it.
* Method logger: Simple method interceptor for automatic model changes detection, it also make use of bytecode instrumentations.

Dragome provides two execution modes, production mode for executing everything in client side, and debug mode that executes all in Java Virtual Machine without compiling to javascript and make remotes updates to browser DOM. 

## System Requirements
* JDK	1.7 or above.
* Memory no minimum requirement.
* Disk space no minimum requirement.
* Operating System no minimum requirement.
* __Important note about debugging__: it is not required the use of any plugin for your IDE or for your browser (any modern Chrome version by now, and any version of Firefox soon, Safari and IE in future versions)

## Client-side code


``` Java
    CrudGrid crudGrid= new CrudGrid(entityType);
	ComponentBuilder componentBuilder= new ComponentBuilder(this);

	componentBuilder.bindTemplate("loading").as(VisualLabel.class).showWhen(crudGrid::isLoading).build();
	componentBuilder.bindTemplate("filter").as(VisualTextField.class).toProperty(crudGrid::getFilter, crudGrid::setFilter).build();
	componentBuilder.bindTemplate("remove-filter").as(VisualLabel.class).disableWhen(() -> crudGrid.getFilter().length() == 0).onClick(v -> crudGrid.setFilter("")).build();
	componentBuilder.bindTemplate("add-mode-toggler").as(VisualLabel.class).onClick(v -> crudGrid.toggleAddMode()).styleWith("glyphicon-minus", "glyphicon-plus").accordingTo(() -> crudGrid.isAddMode()).build();

	componentBuilder.bindTemplate("table-header").as(VisualPanel.class).toList(crudGrid.getColumns()).repeat((column, builder) -> {
	    builder.onClick(() -> crudGrid.setOrderColumn(column)).build();
	    builder.styleWith(column.getStyleName()).when(() -> true);
	    builder.bindTemplate("column-name").as(VisualLabel.class).to(() -> column.getName()).build();
	    builder.bindTemplate("order-icon").as(VisualLabel.class).styleWith("glyphicon-sort-by-alphabet", "glyphicon-sort-by-alphabet-alt").accordingTo(() -> crudGrid.getOrderColumn().getOrder().equals(Order.ASC)).showWhen(() -> crudGrid.getOrderColumn() == column).build();
	});

	componentBuilder.bindTemplate("add-section").as(VisualPanel.class).showWhen(crudGrid::isAddMode).buildChildren(childrenBuilder -> {

	    childrenBuilder.bindTemplate("save-button").as(VisualButton.class).onClick(() -> crudGrid.addObject()).build();
	    childrenBuilder.bindTemplate("remove-button").as(VisualButton.class).onClick(() -> crudGrid.toggleAddMode()).build();

	    childrenBuilder.bindTemplate("columns").as(VisualPanel.class).toList(crudGrid.getColumns()).repeat((column, builder) -> {
		builder.switchUsing(() -> !column.isLookup()).buildChildren(columnBuilder -> {
		    columnBuilder.bindTemplate("input").as(VisualTextField.class).toProperty(() -> crudGrid.getItem().getObject(), column.getName()).switchDefault().disableWhen(() -> column.isAutoincrement()).build();
		    columnBuilder.bindTemplate("select").to(new VisualComboBoxImpl<>(crudGrid.getLookupData(column.getLookupEntityType()))).toProperty(() -> crudGrid.getItem().getObject(), column.getName()).switchWhen(() -> false).showWhen(() -> column.isLookup()).build();
		});
	    });
	});

	componentBuilder.bindTemplate("objects").as(VisualPanel.class).toListProperty(crudGrid::getItems).orderBy(crudGrid.getColumnValueGetter(), () -> crudGrid.getOrderColumn().getOrder()).filter(crudGrid::getFilterTester).repeat((item, itemBuilder) -> {

	    itemBuilder.bindTemplate("toolbar").as(VisualPanel.class).switchUsing(() -> !item.isEditMode()).buildChildren(toolbarChildrenBuilder -> {
		toolbarChildrenBuilder.bindTemplate("view-mode").as(VisualPanel.class).switchDefault().buildChildren(childrenBuilder -> {
		    childrenBuilder.bindTemplate("edit").as(VisualLabel.class).onClick(() -> crudGrid.toggleEditMode(item)).build();
		    childrenBuilder.bindTemplate("trash").as(VisualLabel.class).onClick(() -> crudGrid.deleteObject(item)).build();
		});

		toolbarChildrenBuilder.bindTemplate("edit-mode").as(VisualPanel.class).switchWhen(() -> false).buildChildren(childrenBuilder -> {
		    childrenBuilder.bindTemplate("save").as(VisualLabel.class).onClick(() -> crudGrid.updateObject(item).toggleEditMode(item)).build();
		    childrenBuilder.bindTemplate("remove").as(VisualLabel.class).onClick(() -> crudGrid.toggleEditMode(item)).build();
		});
	    });

	    itemBuilder.bindTemplate("columns").as(VisualPanel.class).toList(crudGrid.getColumns()).repeat((column, columnBuilder) -> {

		columnBuilder.switchUsing(() -> item.isEditMode()).buildChildren(columnChildrenBuilder -> {

		    columnChildrenBuilder.bindTemplate("view-mode").as(VisualPanel.class).switchDefault().onClick(() -> crudGrid.toggleEditMode(item)).switchUsing(() -> column.isLookup()).buildChildren(editModePanelBuilder -> {
			editModePanelBuilder.bindTemplate("not-lookup").as(VisualLabel.class).switchDefault().toProperty(item.getObject(), column.getName()).build();
			editModePanelBuilder.bindTemplate("lookup").as(VisualLabel.class).switchWhen(() -> false).toProperty(item.getObject(), column.getName()).build();
		    });

		    columnChildrenBuilder.bindTemplate("edit-mode").as(VisualPanel.class).switchWhen(() -> true).switchUsing(() -> !column.isLookup()).buildChildren(viewModePanelBuilder -> {
			viewModePanelBuilder.bindTemplate("input").as(VisualTextField.class).toProperty(item.getObject(), column.getName()).switchDefault().disableWhen(() -> column.isAutoincrement()).build();
			viewModePanelBuilder.bindTemplate("select").to(new VisualComboBoxImpl<>(crudGrid.getLookupData(column.getLookupEntityType()))).toProperty(item.getObject(), column.getName()).switchWhen(() -> false).disableWhen(() -> column.isAutoincrement()).build();
		    });
		});
	    });
	});
```


``` html
<body>
<div data-template="loading">Loading...</div>
<div class="input-group col-md-5 row filter">
    <span class="input-group-addon"><i class="glyphicon glyphicon-filter"></i></span>
    <input type="text" class="form-control" data-template="filter">
    <span class="input-group-addon btn btn-default" data-template="remove-filter"><i class="glyphicon glyphicon-remove"></i></span>
</div>

<table class="crud-grid table table-striped table-bordered table-condensed table-hover">
    <tr>
        <th class="col-md-1">
            <div class="btn-toolbar"><i class="btn btn-default glyphicon" data-template="add-mode-toggler"></i></div>
        </th>

        <th data-template="table-header">
            <div>
                <span data-template="column-name"></span>
                <i class="glyphicon" data-template="order-icon"></i>
            </div>
        </th>
    </tr>
    <tr data-template="add-section">
        <td>
            <div class="btn-toolbar">
                <div class="btn-group">
                    <i class="btn btn-default glyphicon glyphicon-save" data-template="save-button"></i>
                    <i class="btn btn-default glyphicon glyphicon-remove" data-template="remove-button"></i>
                </div>
            </div>
        </td>
        <td data-template="columns">
            <input class="form-control" data-template="input"/>
            <select data-template="select" class="form-control"></select>
        </td>
    </tr>
    <tr data-template="objects">
        <td>
            <div class="btn-toolbar" data-template="toolbar">
                <div class="btn-group" data-template="view-mode">
                    <i class="btn btn-default glyphicon glyphicon-edit" data-template="edit"></i>
                    <i class="btn btn-default glyphicon glyphicon-trash" data-template="trash"></i>
                </div>
                <div class="btn-group" data-template="edit-mode">
                    <i class="btn btn-default glyphicon glyphicon-save" data-template="save"></i>
                    <i class="btn btn-default glyphicon glyphicon-remove" data-template="remove"></i>
                </div>
            </div>
        </td>
        <td data-template="columns">
            <div data-template="view-mode">
                <span data-template="not-lookup"></span>
                <span data-template="lookup"></span>
            </div>

            <div data-template="edit-mode">
                <input class="form-control" data-template="input"/>
                <select data-template="select" class="form-control"></select>
            </div>
        </td>
    </tr>
</table>
</body>
```
