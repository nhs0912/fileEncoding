파일 변환 프로젝트(ChangeEncoding)
=================================


설명  
---
#### [파일 인코딩 변환]

* 프로젝트 안에서 매번 에디터를 이용해서 파일 인코딩을 변경하고 있다. 
이 문제를 조금 더 편리하게 하고자 UTF-8 -> EUC-KR로 변환하는 프로젝트를 
만들기로 하였다. 
그래서 편리함이 제일 큰 목적으로 이 프로젝트를 만들기로 결정함!
오직 사용자만을 위한 프로젝트! 

기능
-------
- [ ] 파일 업로드 
- [ ] UTF-8 <---> EUC_KR로 변환
- [ ] original File과 변환된 파일 저장_optional
- [ ] 파일 정보 DB 저장

프로세스
-----

1. 파일 업로드를 웹 브라우저에 drag and drop으로 선택한다.
2. drop 하자마자 파일 인코딩 변환시작하고 준비 되면 다운로드 버튼 활성화
3. DB에도 파일 정보 저장
4. download 하였을 때 DB 컬럼 다운로드 여부 표시 

Entity
---

* MyFile
   * File에 대한 정보를 가지고 있는 domain