@import owner.buriedoiltank.pages.PageModels.Breadcrumb
@param java.util.List<Breadcrumb> breadcrumbs
<nav class="breadcrumbs" aria-label="Breadcrumb">
    @for (var crumb : breadcrumbs)
        <a href="${crumb.path()}">${crumb.label()}</a>
    @endfor
</nav>
