[#ftl/]
[#import "/common.ftl" as common]
[#import "/spring.ftl" as spring]
[#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]]
[@tiles.importAttribute/]
[#--][@tiles.useAttribute name="selectedIndex"/]--]
[#--Selected Index: [@tiles.insertAttribute name="selectedIndex"/]--]
[@tiles.importAttribute name="selectedIndex"/]
[@common.subHeader headers={"Thiết lập cho site": "/admin/site-settings", "Quản lí subject/league": "/admin/subjects", "Quản lí điều hành viên": "/admin/moderators"} selectedIndex=selectedIndex/]
[@tiles.insertAttribute name="subBody" /]
