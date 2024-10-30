import { Col, Flex, Form, Modal, Row, Select, Table, Tabs, message, notification } from "antd";
import { Link } from "react-router-dom";
const Footer = () => {
    return (
        <footer >
            <div style={{ textAlign: 'center', padding: 30, backgroundColor: '#222831', color: '#a7a7a7', marginTop: 50 }}>
                <Row >
                    <Col span={8} style={{ marginTop: 50 }}>
                        <img src="/logo.svg" alt="" width={200} />
                    </Col>
                    <Col span={8}>
                        <h2>Về Chúng Tôi</h2>
                        <Flex gap='middle' vertical style={{ fontSize: 16, textAlign: 'center', marginTop: 10 }}>
                            <span><Link to={'/'} style={{ color: 'rgb(167, 167, 167)' }}>Trang Chủ</Link></span>
                            <span>Về chúng tôi</span>
                            <span>Liên hệ</span>
                            <span><Link to={'/job'} style={{ color: 'rgb(167, 167, 167)' }}>Công việc</Link></span>
                        </Flex>

                    </Col>
                    <Col span={8}>
                        <h2>Điều khoản và điều kiện</h2>
                        <Flex gap='middle' vertical style={{ fontSize: 16, textAlign: 'center', marginTop: 10 }}>
                            <span>Chính sách bảo mật</span>
                            <span>Quy định hoạt động</span>
                            <span>Xử lý khiếu nại</span>
                            <span>Báo chí</span>
                        </Flex>

                    </Col>
                </Row>
                <div style={{ marginTop: 60 }}>
                    Copyright © Frace | Liên hệ: 0312192258
                </div>
            </div>

        </footer>



    );
}

export default Footer;