[#ftl/]
[#import "/spring.ftl" as spring/]
[#import "/common.ftl" as common/] 
<div class="module">
	<div class="description-box">
		<img src="[@spring.url "${league.logoUrl}"/]" width="96" height="64" class="fl"/>
				<p>${league.description}</p>
				<p>Có <strong>300</strong> thanh vien hiện được phép tham gia giải đấu</p>
				<p>Có <strong>35</strong> thành viên đang thi đấu</p>
		</table>
	</div>
	<div class="description-box">
		<p>
		Ban dang co <strong>40</strong> diem.
		[#if nextLeague?exists] 
		Ban con <strong>20</strong> ngay de tich luy <strong>100</strong> diem de co the tru hang hoac tich luy du <strong>300</strong> diem de co the tham du
		<a href="[@spring.url "/leagues/${nextLeague.autoId}/${nextLeague.alias}"/]">${nextLeague.name}</a>
		[/#if]
		</p>
	</div>
</div>

[#--
rankItem
a macro for displaying a ranking item
--]
[#macro rankItem no member score matches]
	<tr><td>${no}</td><td><a href="/users/${member}">${member}</a></td><td class="tar">${score}</td><td class="tar">${matches}</td></tr>
[/#macro]
<div class="module">
	<h3>Bảng xếp hạng</h3>
	<div>
	<table class="fw grid">
	<tr><th>Hạng #</th><th>Thành viên</th><th>Điểm</th><th>Trận</th></tr>
	[@rankItem no="1" member="mrcold" score="3900" matches="1000"/]
	[@rankItem no="2" member="mica" score="3800" matches="1000"/]
	[@rankItem no="3" member="bond" score="3450" matches="1000"/]
	[@rankItem no="4" member="tanj" score="302" matches="100"/]
	[@rankItem no="5" member="kiniko" score="295" matches="100"/]
	[@rankItem no="6" member="mai_chee" score="290" matches="50"/]
	[@rankItem no="7" member="kenya" score="293" matches="50"/]
	[@rankItem no="8" member="char" score="290" matches="50"/]
	[@rankItem no="9" member="nany" score="252" matches="50"/]
	[@rankItem no="10" member="hen" score="230" matches="50"/]
	</table>
	</div>
</div>