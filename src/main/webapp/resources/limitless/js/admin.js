var selectedImagePathStr;
var insertImage=false;
var ckeditorinstance='pageForm:ckeditor';
function editorInsertImage(xhr, status, args) {
    selectedImagePathStr= args.selectedImagePathStr;
    //parse it, process it and load it into the chart
    console.log(selectedImagePathStr);
    if (insertImage) {
        CKEDITOR.instances[ckeditorinstance].insertHtml("<img src='" + selectedImagePathStr + "' width='400' />");
    }
    insertImage=false;
}


function getWidgetVarById(id) {
    for (var propertyName in PrimeFaces.widgets) {
        if (PrimeFaces.widgets[propertyName].id === id) {
            return PrimeFaces.widgets[propertyName];
        }
    }
}