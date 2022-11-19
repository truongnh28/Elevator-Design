# Elevator System

Date: November 20, 2022 12:28 AM
Topic: Back-end

Our company is building a new campus, your job is to write a backend application for its elevator system. For simplicity, let's assume that we need to control a system with n cabinets, to serve m floors (your application's parameters). On each floor there will be one panel (client) for customers to send their requests to your backend.

Evaluating criteria

- It should work on your local machine
- It should handle simultaneous requests from clients gracefully
- The API should be exposed publicly (and well documented), so your evaluators can call it from his/her local machine
- Have tests, careful tests
- Using Docker is a big plus, but other deployment methods is okay too, as long as you use IaaS
- A working domain is a plus
Integrating with monitoring solutions is a plus

Notes 🗒:

- Take any tech stack you want. But make sure you know it well, not just get it up and running.

### Analysis

1. Yêu cầu ban đầu:
    - Thang máy có n cabin phục vụ cho m tầng.
    - Mỗi thang máy không được chở quá 15 người cùng một lúc.
    - Những người ở một tầng nhất định thì nên đi thang máy gần nhất.
    - Hiển thị từng tầng và số lượng người muốn vào thang máy.
    - Mỗi tầng phải có menu chọn và nút để chọn tầng mà người đó muốn đi đến.
    - Mỗi tầng phải hiện thị từng trạng thái của thang máy. Số người trong thang máy hiện tại, tầng hiện tại của từng thang máy và hướng hiện tại của thang máy.
2. Yêu cầu mở rộng / Giả định
    
    Giả định dưới đây được đưa ra để làm rõ các yêu cầu và đặc biệt được coi là không mâu thuẫn với các yêu cầu ban đầu:
    
    - Mỗi tầng có một bảng nút duy nhất để gọi thang máy.
    - Tất cả những người trên một tầng phải đến cùng một tầng đích, tức là tất cả những người ở tầng 3, sẽ được đưa lên tầng 9
    - Tại một thời điểm nhất định, trên một tầng nhất định, những người trên tầng đó chỉ có thể nhấn một nút.
    - Thang máy có thể dừng giữa các tầng, có người muốn xuống thang hoặc có người muốn lên thang máy (với điều kiện thang máy không đầy)
    - Thang máy sẽ dừng nếu không có lệnh ghé thăm nữa.
    - Nếu có nhiều người trên một tầng hơn số người có thể chứa trong thang máy, thì thang máy sẽ lấy những người có thể vừa trong thang máy để lại những người thừa trên tầng
    - Mọi người không thể sử dụng thang máy để đi trên cùng một tầng như họ đã ở trên
    - Thuật toán lựa chọn thang máy phụ thuộc vào hướng của thang máy và khoảng cách từ tầng đích. Về bản chất, thang máy đi lên sẽ không thực hiện bất kỳ lệnh nào cho bất kỳ tầng nào bên dưới nó trừ khi nó ở tầng cuối cùng theo hướng đó. Thang máy đi lên sẽ chỉ nhận lệnh cho tầng mà nó chưa đạt (theo hướng đi lên). Quy tắc tương tự áp dụng cho thang máy đi xuống
    - Trong trường hợp có sự ràng buộc trong việc lựa chọn thang máy (đối với hai thang máy đứng yên), một thang máy được chọn ngẫu nhiên sẽ được sử dụng để phục vụ con người.
    - Thang máy chỉ dừng ở một tầng nếu nó có lệnh dừng ở tầng đó.
    - Tầng đích không thể thay đổi bởi những người bên trong thang máy, nó chỉ được chọn tại thời điểm lên thang máy.
    - Thang máy sẽ dừng ở tất cả các tầng mà nó đã nhận lệnh, theo thứ tự xuất hiện của chúng (tức là lên / xuống) chỉ trong 5 giây. Thang máy sẽ không dừng ở tầng / tầng mà không có lệnh nào cho nó.
    - Thang máy sẽ mất 1 giây để di chuyển từ tầng 1 đến tầng tiếp theo theo bất kỳ hướng nào
3. Ghi chú thiết kế
    - Hệ thống thang máy bao gồm Bộ điều khiển thang máy và một bộ Thang máy
    - Mỗi tầng đều có một bảng tổng đài được người dân sử dụng để tương tác với hệ thống thang máy.
    - Khi nhấn một nút, Bộ điều khiển thang máy sẽ thêm đơn hàng mới trong danh sách đơn hàng Thang máy gần nhất.
    - Thang máy sắp xếp các lệnh nhận được từ Bộ điều khiển thang máy.
    - Thang máy thông báo Elevator-Controller cho mỗi tầng mà họ đến.
    - Nhân viên điều khiển thang máy luôn ghi chép về vị trí và hướng đi của thang máy. Thang máy đứng yên sẽ không có bất kỳ hướng nào.
    - Thang máy sẽ theo dõi mọi người và mức độ điểm đến của họ. Vậy giả sử có 10 người đi thang máy từ tầng trệt đi lên tầng 10, sau đó trên đường đi lên có 5 người nữa lên thang máy từ tầng 2 đến tầng 9, khi đó thang máy phải dừng ở tầng 9, tự loại bỏ 5 người thì lên tầng 10 và loại bỏ 10 người.
4. Diagram
    
    a. Usecase Diagram:
    
    ![Untitled](Elevator%20System%20faf77e4a7a8b4d5695b0b781624df535/Untitled.png)
    
    ![Untitled](Elevator%20System%20faf77e4a7a8b4d5695b0b781624df535/Untitled%201.png)
    
    ![Untitled](Elevator%20System%20faf77e4a7a8b4d5695b0b781624df535/Untitled%202.png)
    
    b. Component Diagram
    
    ![Untitled](Elevator%20System%20faf77e4a7a8b4d5695b0b781624df535/Untitled%203.png)
    
    c. Class Diagram
    
    ![Untitled](Elevator%20System%20faf77e4a7a8b4d5695b0b781624df535/Untitled%204.png)
    

### Solution

1. Cách hoạt động
    
    Một kịch bản đơn giản là mọi người đến một tầng (addPeopleOnFloor) và yêu cầu thang máy cho tầng đích (requestElevator). Khi Thang máy dừng ở một tầng, mọi người ra khỏi thang máy trước, sau đó những người đang chờ ở tầng này lên thang máy và Đặt hàng cho tầng mới và việc này tiếp tục. Để kiểm tra hệ thống, người ta có thể sử dụng một ứng dụng khách REST như PostMan.
    
2. API Document
    1. Run in localhost:
        
        [elevator-api-document](https://documenter.getpostman.com/view/21317205/UzBiNTfR)
        
    2. Run in vps:
        
        [elevator-api-document](https://documenter.getpostman.com/view/21317205/UzBiNTfW)
        

### Techniques

- Java
- Java Spring boot
- UnitTest & Mockito
- Docker
- Rest API
- VPS
- Nginx

### Source code