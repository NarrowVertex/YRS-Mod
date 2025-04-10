# YRS-Mod
마인크래프트 서버 'YRS Online' 전용 모드

![YRS-Mod Overview](/images/1.%20ingame1.png)

## 기술 스택
![Java](https://img.shields.io/badge/Java%207-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white)
![Forge](https://img.shields.io/badge/Forge-E04E14?style=flat-square&logo=minecraft&logoColor=white)

## 기능 설명
- RPG 게임에 필요한 시스템 전반을 개발
    - RPG용 커스텀 인게임 인터페이스
        - 게임 ‘디아블로2’를 참고한 커스텀 인게임 인터페이스
        - RPG의 주요 스텟인 체력, 마나, 레벨을 가장 두드러지게 보여줌
        - 장착된 스킬과 포션을 퀵뷰로 보여주며 캐릭터 정면 모습도 보여주어 더 게임에 몰입감이 들게함
        - 기존에 있던 인벤토리 칸과 배고픔, 갑옷도 리뉴얼하고 재배치하여 더 RPG에 맞게 수정
    - 스킬 관리 시스템
        - 각 직업에 맞는 스킬리스트를 보여줌
        - 각 스킬은 SP를 소모하여 해금하거나 업그레이드 가능
        - 사용가능한 스킬은 하단의 스킬창에 드래그로 등록하여 인게임에서 단축키로 사용가능
    - 포션 및 퀵 포션 슬롯
        - RPG에 맞는 포션 추가(HP, MP 포션)
        - 기본 게임 아이템처럼 사용가능하고, 퀵 포션 슬롯에 등록하는 것으로 단축키를 통해 빠르게 사용가능
    - 스텟(힘, 방어, 명중률, 회피률)
        - 각 캐릭터마다 스텟 4가지 존재
        - STR(힘): 스텟마다 데미지 증가
        - DEF(방어): 스텟마다 받는 데미지 감소
        - ACC(정확도): 스텟마다 치명타 확률 증가
        - AGI(회피): 스텟마다 공격 회피률 증가
    - config&DB를 이용해 데이터 수정을 간편화
        - 각 직업, 캐릭터, 스킬 등의 여러 인게임 데이터를 컨피그화하여 누구든지 수정하기 용이하게 만들어 서버 관리자가 더 쉽게 관리할 수 있도록 함
    - 타 모드/플러그인과 로컬 서버를 이용한 상호작용
        - RPG에 필요한 타 애드온(모드/플러그인)과 로컬 서버를 이용해 데이터를 주고받으며 상호작용
        - 한 에드온으로는 한계가 있는 기능을 확장해 더 많은 서비스를 제공 가능
        
## 설치 및 실행방법

### Forge
1. 다음 링크에서 Forge 1.7.10 Recommended 인스톨러 다운로드  
[Forge 사이트](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.7.10.html)

2. 인스톨러를 실행해서 설치

3. %appdata%/.minecraft에 Mods 폴더를 생성

4. YRS 모드를 폴더에 넣고 마인크래프트 실행